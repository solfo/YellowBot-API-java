package com.yellowbot.api;

import java.util.Map;

/**
 * <p>
 * This interface represents the API for client-side communication with YellowBot.
 * The following illustrates its usage.
 * </p>
 *
 * <code><pre>
 *     import com.yellowbot.api.YellowBot;
 *     import com.yellowbot.api.Client;
 *
 *     Client client = YellowBot.newApiClient(apiKey, apiSecret);
 *
 *     Map result = client.call(endPoint, callParams);
 *     if ((Boolean)result.get("success")) {
 *         // process result
 *     }
 *     else {
 *         // handle failure
 *     }
 *</pre></code>
 *
 * <h2>Mapping the JSON Response</h2>
 *
 * <p>
 *     The output of the API calls are enclosed in a map
 *     which contains numbers, strings, booleans, other maps
 *     and lists. The structure varies with the end point,
 *     parameters, error conditions, and as the API evolves.
 *     So it is not very feasible to hold the response
 *     in fixed containers as typical POJOs.
 * </p>
 *
 * <p>
 *     So this code does not try to do that. Instead
 *     it maps dynamically the JSON response
 *     into a data structure made of primitives,
 *     maps and lists. This works like with Jackson
 *     simple data binding approach
 *     ({@link <a href="http://wiki.fasterxml.com/JacksonInFiveMinutes#Simple_Data_Binding_Example">http://wiki.fasterxml.com/JacksonInFiveMinutes#Simple_Data_Binding_Example</a>}).
 *     For example, consider the following piece of JSON.
 * </p>
 *
 * <code><pre>
 *     {
 *      "name" : { "first" : "Joe", "last" : "Sixpack" },
 *      "gender" : "MALE",
 *      "verified" : false,
 *      "userImage" : "Rm9vYmFyIQ=="
 *     }
 * </pre></code>
 *
 * <p>
 *     If this comes as the response of some API call,
 *     the resulting map is just like the one we would
 *     construct explicitly by:
 * </p>
 *
 * <code><pre>
 *     Map<String,Object> userData = new HashMap<String,Object>();
 *     Map<String,String> nameStruct = new HashMap<String,String>();
 *     nameStruct.put("first", "Joe");
 *     nameStruct.put("last", "Sixpack");
 *     userData.put("name", nameStruct);
 *     userData.put("gender", "MALE");
 *     userData.put("verified", Boolean.FALSE);
 *     userData.put("userImage", "Rm9vYmFyIQ==");
 * </pre></code>
 *
 * <p>
 *     This API client gives a helping hand to mark when
 *     a response is an error response or not.
 *     Instead of looking for <code>error</code> or <code>system_error</code> keys,
 *     just look for a boolean in <code>success</code> field.
 *     This explains the pattern for handling the response
 *     of {@link Client#call}:
 * </p>
 *
 *<code><pre>
 *     Map result = client.call(endPoint, callParams);
 *     if ((Boolean)result.get("success")) {
 *         // process result
 *     }
 *     else {
 *         // process failure
 *     }
 *</pre></code>
 *
 * <h2>Exceptions</h2>
 *
 * <p>
 * The constructor ({@link YellowBot#newApiClient}) and the {@link #call} method can throw exceptions,
 * more specifically instances of {@link APIException} or its subclasses.
 *
 * </p>
 *
 * <p>
 *     These exceptions are purposefully unchecked exceptions
 *     (derived from {@link RuntimeException}). These avoid the need
 *     for wrapping the API calls in try-catch constructions
 *     or the proliferation of throws clauses in method declarations.
 * </p>
 *
 * <p>
 *     However, there is no magic. An HTTP client (like the YellowBot API client is)
 *     can throw exceptions due to multiple possible issues: network,
 *     server availability, etc. So be prepared down the line
 *     to handle them somewhere.
 * </p>
 *
 * <p>
 * Copyright 2011 Solfo Inc, all rights reserved.
 * </p>
 *
 * <!--
 * Created by IntelliJ IDEA.
 * Date: 10/16/11 3:09 PM
 * -->
 */
public interface Client {

    /**
     * The API server in use.
     * @return the corresponding host
     */
    String getApiServer();

    /**
     * Assigns a host as the API server for this client.
     *
     * @param apiServer the host to be used as API server
     * @throws IllegalArgumentException If <code>apiServer</code> is invalid.
     */
    void setApiServer(String apiServer);

    /**
     * Makes an API call for the given API end point
     * with the given parameters.
     *
     * @param endPoint the API endpoint, eg. <code>"reputation_management/basedata"</code>
     * @param params the arguments for the call
     * @return a data structure representing the response
     *
     * @throws IllegalArgumentException If <code>endPoint</code> is invalid.
     * @throws APIException on errors building the request
     * @throws APIException on errors parsing/mapping JSON response
     */
    Map call( String endPoint, Map params );

    /**
     * Builds a signin URL for the given parameters.
     *
     * @param params the arguments for the signin
     * @return the signin URL as a String
     */
    String buildSigninUrl(Map params);
}
