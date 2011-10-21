package com.yellowbot.api;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * <p>
 * Copyright 2011 Solfo Inc, all rights reserved.
 * </p>
 *
 * <!--
 * Created by IntelliJ IDEA.
 * Date: 10/21/11 9:38 PM
 * -->
 */
public class UtilsTest {
    @Test
    public void testCheckHost() throws Exception {
        assertTrue("www.yellowbot.com is a good host", Utils.checkHost("www.yellowbot.com"));
        assertTrue("yellowbot.com is a good host", Utils.checkHost("yellowbot.com"));
        assertTrue("www.weblocal.ca is a good host", Utils.checkHost("www.weblocal.ca"));
        assertTrue("weblocal.ca is a good host", Utils.checkHost("weblocal.ca"));
        assertFalse("blah! is a bad host", Utils.checkHost("blah!"));
        assertFalse("foo is a bad host", Utils.checkHost("foo"));
    }

    @Test
    public void testCheckEndPoint() throws Exception {
        assertTrue("test/echo is a good end point", Utils.checkEndPoint("test/echo"));
        assertFalse("/test/echo is a bad end point", Utils.checkEndPoint("/test/echo"));
        assertFalse("test/echo/ is a bad end point", Utils.checkEndPoint("test/echo/"));
        assertFalse("/test/echo/ is a bad end point", Utils.checkEndPoint("/test/echo/"));
        assertTrue("reputation_management/basedata is a good end point", Utils.checkEndPoint("reputation_management/basedata"));
        assertTrue("class/method/submethod is a good end point", Utils.checkEndPoint("class/method/submethod"));
    }
}
