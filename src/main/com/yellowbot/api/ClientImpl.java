package com.yellowbot.api;


import org.apache.commons.codec.binary.Hex;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * Default implementation of {@link Client}.
 *
 * <p>
 * This part of the API is subject to change.
 * </p>
 *
 * <p>
 * Copyright 2011 Solfo Inc, all rights reserved.
 * </p>
 *
 * <p/>
 * <!--
 * Created by IntelliJ IDEA.
 * Date: 10/16/11 3:33 PM
 * -->
 */
public class ClientImpl implements Client {

    /**
     * The API version in use.
     */
    private int apiVersion = 2;

    /**
     * The API server in use.
     */
    private String apiServer = "www.yellowbot.com";

    /**
     * The API key in use.
     */
    private String apiKey;

    /**
      * The engine to compute the signature parameter.
      *
      */
    private Mac mac;

    /**
     * The HTTP client which really does the heavy work.
     */
    private HttpClient httpClient;

    /**
     * The JSON parser/mapper.
     */
    private ObjectMapper jMapper;


    private ClientImpl() {
        httpClient = new DefaultHttpClient();
        jMapper = new ObjectMapper();
    }

    /**
     *
     * @param apiKey
     * @param apiSecret
     * @throws APIException If apiSecret is detected to be invalid
     */
    public ClientImpl( String apiKey, char[] apiSecret ) {
        this();

        this.apiKey = apiKey;

        try {
            byte[] apiSecretBytes = Utils.getUtf8Bytes(apiSecret);
            mac = Mac.getInstance("HmacSHA256");
            SecretKey ks = new SecretKeySpec(apiSecretBytes, mac.getAlgorithm());
            mac.init(ks);
        } catch (NoSuchAlgorithmException e) {
            throw new InternalException(e); // What do you mean? No HmacSHA256?!
        } catch (InvalidKeyException e) {
            throw new APIException("Invalid parameter apiSecret", e);
        }
    }

    // setters & getters

    public String getApiServer() {
        return apiServer;
    }

    public void setApiServer(String apiServer) {
        if (!Utils.checkHost(apiServer))
            throw new IllegalArgumentException("Invalid parameter apiServer ("+apiServer+")");
        this.apiServer = apiServer;
    }

    public int getApiVersion() {
         return apiVersion;
     }

     public void setApiVersion(int apiVersion) {
         if (apiVersion < 1)
             throw new IllegalArgumentException("Invalid parameter apiVersion ("+apiVersion+")");
         this.apiVersion = apiVersion;
     }

    // other methods

    public Map call(String endPoint, Map params) {
        if (!Utils.checkEndPoint(endPoint))
             throw new IllegalArgumentException("Invalid parameter endPoint ("+endPoint+")");
        try {
            return doCall(endPoint, params);
        } catch (URISyntaxException e) {
            throw new APIException("Error building request", e);
        } catch (JsonProcessingException e) {
            throw new APIException("Error processing JSON", e);
        } catch (IOException e) {
            throw new APIException("Error executing request", e);
            // also receiving response
        }
    }

    private Map doCall(String endPoint, Map params) throws URISyntaxException, IOException {
        // Build request
        HttpUriRequest request = buildRequest(endPoint, params);

        // Execute
        HttpResponse response = httpClient.execute(request);

        // Interpret response
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            String mimeType = EntityUtils.getContentMimeType(entity);
            if (mimeType.matches("text/json|text/javascript")) {
                Map result = jMapper.readValue(entity.getContent(), Map.class);
                result = augmentResult(result);
                return result;
            }
            else {
                String content = EntityUtils.toString(entity);
                if (content.indexOf("system_error") != -1) {
                    Map result = jMapper.readValue(content, Map.class);
                    return augmentResult(result);
                }
                return null; // FIXME
            }
        }
        return null;    //FIXME
    }


    private static Map augmentResult( Map result ) {
        if (result.containsKey("success"))
            return result;
        if (result.containsKey("error") || result.containsKey("system_error"))
            result.put("success", false);
        else
            result.put("success", true);
        return result;
    }


     /**
     * Constructs a hash given the password and the salt using the Hmac SHA256
     * encoding algorithm.
     *
     * @param source The pre-encoded source string.
     * @return An encrypted <code>String</code> using the Hmac SHA256
     *         algorithm.
     */
     private String buildHash(String source) {
            mac.reset();
            return Hex.encodeHexString(mac.doFinal(Utils.getUtf8Bytes(source)));

     }

    private String buildHash( List<NameValuePair> params ) {
        String paramString = buildParameterString(params);
        return buildHash(paramString);
    }



    private static String buildParameterString(List<NameValuePair> params) {

        StringBuilder buf = new StringBuilder();

        Comparator<NameValuePair> cmp = new Comparator<NameValuePair>() {
            public int compare(NameValuePair e1, NameValuePair e2) {
                return e1.getName().compareTo(e2.getName());
            }
        }   ;
        Set<NameValuePair> pairs = new TreeSet<NameValuePair>(cmp);
        pairs.addAll(params);

        for ( NameValuePair e : pairs) {
            if ("api_sig".equals(e.getName()))
                continue;
            buf.append(e.getName());
            buf.append(e.getValue());
        }

        return buf.toString();
    }

    private final String API_PATH_ROOT = "/api/";

    private HttpUriRequest buildRequest(String endPoint, Map params) throws URISyntaxException {
        // doGet
        return buildGetRequest(endPoint, params);
    }

    private HttpUriRequest buildGetRequest(String endPoint, Map params) throws URISyntaxException {
        String apiPath = API_PATH_ROOT + endPoint;

        // build query
        List<NameValuePair> qparams = new ArrayList<NameValuePair>();
        Set<Map.Entry> pairs = params.entrySet();
        for (Map.Entry e : pairs) {
            qparams.add(new BasicNameValuePair(e.getKey().toString(), e.getValue().toString()));

        }
        // api_key
        qparams.add(new BasicNameValuePair("api_key", apiKey));

        // api_ts
        qparams.add(new BasicNameValuePair("api_ts", ""+Utils.theTime()));

        // api_sig
        qparams.add(new BasicNameValuePair("api_sig", buildHash(qparams)));

        String query = URLEncodedUtils.format(qparams, "UTF-8");

        URI uri = URIUtils.createURI("http", apiServer, -1, apiPath, query, null);
        HttpGet httpGet = new HttpGet(uri);

        //System.out.println(httpGet.getURI()); // DEBUG

        return httpGet;
    }

    public String buildSigninUrl(Map params) {
        throw new RuntimeException("Not implemented yet");
    }
}
