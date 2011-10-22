package com.yellowbot.api;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by IntelliJ IDEA.
 * User: ferreira
 * Date: 10/21/11
 * Time: 10:12 PM
 * To change this template use File | Settings | File Templates.
 */
public class ClientAccessorTest {
    @Test
    public void testApiServer() throws Exception {
        Client client = YellowBot.newApiClient("api_key", new char[] { 's', 'e','c','r','e', 't'});
        client.setApiServer("www.yellowbot.dk");
        assertEquals("Assigning www.yellowbot.dk as API server is okay", client.getApiServer(), "www.yellowbot.dk");
    }

    @Test
    public void testApiVersion() throws Exception {
        Client client = YellowBot.newApiClient("api_key", new char[] { 's', 'e', 'c', 'r', 'e', 't' });
        client.setApiVersion(3);
        assertEquals("Assigning 3 as API version is okay", client.getApiVersion(), 3);
        client.setApiVersion(1);
        assertEquals("Assigning 1 as API version is okay", client.getApiVersion(), 1);
        client.setApiVersion(2);
        assertEquals("Assigning 2 as API version is okay", client.getApiVersion(), 2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBadApiVersion() {
        Client client = YellowBot.newApiClient("api_key", new char[] { 's', 'e', 'c', 'r', 'e', 't' });
        client.setApiVersion(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOtherBadApiVersion() {
        Client client = YellowBot.newApiClient("api_key", new char[] { 's', 'e', 'c', 'r', 'e', 't' });
        client.setApiVersion(-1);
    }
}
