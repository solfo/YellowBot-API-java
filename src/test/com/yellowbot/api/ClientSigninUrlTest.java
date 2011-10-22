package com.yellowbot.api;

import org.apache.http.client.utils.URLEncodedUtils;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

/**
 * * <p>
 * Copyright 2011 Solfo Inc, all rights reserved.
 * </p>
 *
 * <p/>
 * <!--
 * Created by IntelliJ IDEA.
 * Date: 10/21/11 11:12 PM
 * -->
 */
public class ClientSigninUrlTest {
    @Test
    public void testBuildSigninUrl() throws Exception {
        Client client = YellowBot.newApiClient("api_key", new char[] { 'a','p','i','_','s','e','c','r','e','t'});

        Map params = new HashMap();
        params.put("api_ts", 1319244732);
        params.put("api_user_identifier", "agent_x");
        params.put("yp_r", "/merchant");
        String url = client.buildSigninUrl(params);
        String expectedUrl = "http://www.yellowbot.com/signin/partner?" +
                "api_ts=1319244732&" +
                "yp_r=%2Fmerchant&" +
                "api_user_identifier=agent_x&" +
                "api_key=api_key&"+
                "api_sig=8628b9d1f0721479cf0f27fee17d53c4503e72234caa3b8fbc4761267bfe6d76";

        // the signing URL is invariant given the same parameters (including api_ts)
        assertEquals(url, expectedUrl);

        // TODO: improve this test - the real invariant is the set of pairs; the order is not important
    }

}
