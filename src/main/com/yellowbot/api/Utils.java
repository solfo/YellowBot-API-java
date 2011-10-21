package com.yellowbot.api;

import java.nio.CharBuffer;
import java.nio.charset.Charset;

/**
 * A collection of utilities.
 *
 * <p>
 * No user serviceable parts inside. This part of the API is subject to change.
 * </p>
 *
 * <p>
 * Copyright 2011 Solfo Inc, all rights reserved.
 * </p>
 *
 * <!--
 * Created by IntelliJ IDEA.
 * Date: 10/17/11 7:23 PM
 * -->
 */
public class Utils {

    /**
     * The number of non-leap seconds since the epoch: 00:00:00 UTC, January 1, 1970
     * @return
     */
    public static long theTime() {
        return System.currentTimeMillis() / 1000;
    }

    // Converting chars into UTF-8 bytes

    private static final Charset theUtf8Charset;
    static {
        theUtf8Charset = Charset.forName("UTF-8") ;
    }

    public static byte[] getUtf8Bytes(char[] array) {
        return theUtf8Charset.encode(CharBuffer.wrap(array)).array();
    }

    public static byte[] getUtf8Bytes(String s) {
        return s.getBytes(theUtf8Charset);
    }

    /**
     * Checks if <code>host</code> is valid. For example,
     * <code>"www.yellowbot.com"</code> or <code>"122.22.22.33:80</code>
     * are valid, while <code>"blah!"</code> is not.
     * @param host
     * @return <code>true</code> if <code>host</code> looks valid; <code>false</code> otherwise.
     */
    public static boolean checkHost( String host ) {
        return host!=null && host.matches("(\\w+\\.)+\\w+(:\\d+)?");
    }

    /**
     * Checks if <code>endPoint</code> is valid. For example,
     * <code>"test/echo"</code> looks good, while <code>"/foo/bah"</code>
     * and <code>"rep/"</code> are not.
     *
     * @param endPoint
     * @return <code>true</code> if <code>endPoint</code> looks valid; <code>false</code> otherwise.
     */
    public static boolean checkEndPoint( String endPoint ) {
        return endPoint!=null && endPoint.matches("(\\w+/)+\\w+");
    }
}
