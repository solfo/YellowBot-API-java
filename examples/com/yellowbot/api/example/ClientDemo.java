package com.yellowbot.api.example;

import com.yellowbot.api.Client;
import com.yellowbot.api.YellowBot;

import java.util.HashMap;
import java.util.Map;

/**
 * Run like
 * <code>
 * java -cp yellowbot-api/ com.yellowbot.api.example.ClientDemo  US api_key api_secret
 * </code>
 * <p/>
 *
 * <p>
 * Copyright 2011 Solfo Inc, all rights reserved.
 * </p>
 *
 * <p/>
 * <!--
 * Created by IntelliJ IDEA.
 * Date: 10/17/11 3:14 PM
 * -->
 */
public class ClientDemo {

    public static void main(String[] args) {

        if (args.length < 3) {
            System.err.println("Usage: java ClientDemo <country> <api_key> <api_secret>");
            System.exit(0);
        }

        String country = args[0];
        String apiKey = args[1];
        char[] apiSecret = args[2].toCharArray();

        if (!country.matches("US|CA")) {
            System.err.println("<country> should be one of \"US\" or \"CA\"");
            System.exit(0);
        }

        Client client = YellowBot.newApiClient(apiKey, apiSecret);

        if ("US".equals(country)) {
            demoEcho(client);
            //demoSearch(client);
            //demoBadCall(client);
        }
        if ("CA".equals(country)) { // meanwhile in Canada
            client.setApiServer("www.weblocal.ca");
            demoHalifaxSearch(client);
        }
    }

    /**
     * Basic API demo: echo request parameters.
     *
     * @param client
     */
    private static void demoEcho(Client client) {
        Map params = new HashMap();
        params.put("location", "gordon-biersch-burbank-ca");
        params.put("user", "xyz_19282")     ;
        Map data = client.call("test/echo", params);
        if (!(Boolean) data.get("success")) {
            System.out.println("Ooops, unexpected failure");
        }

        System.out.println(data);
    }

    /**
     * Search some sashimi @ Anchorage, AK
     *
     * @param client
     */
    private static void demoSearch(Client client) {
        Map params = new HashMap();
        params.put("q", "sashimi");
        params.put("place", "Anchorage, AK");
        params.put("limit", 10);
        Map data = client.call("search/locations", params);
        if (!(Boolean) data.get("success")) {
            System.out.println("Ooops, unexpected failure");
        }

        System.out.println(data);
    }

    /**
     * A local search in Canada: some sushi @ Halifax, NS
     *
     * @param client
     */
    private static void demoHalifaxSearch(Client client) {
        Map params = new HashMap();
        params.put("q", "sushi");
        params.put("place", "Halifax, NS");
        params.put("limit", 10);
        Map data = client.call("search/locations", params);
        if (!(Boolean) data.get("success")) {
            System.out.println("Ooops, unexpected failure");
        }

        System.out.println(data);
    }

    /**
     * A bad API calls results in failure.
     *
     * @param client
     */
    private static void demoBadCall(Client client) {
        Map params = new HashMap();
        params.put("q", "crystal");
        params.put("place", "Boca Raton, FL");
        params.put("limit", 10);
        Map data = client.call("search/loctions", params);
        if ((Boolean) data.get("success")) {
            System.out.println("Ooops, unexpected success");
       }

        System.out.println(data);

    }

}
