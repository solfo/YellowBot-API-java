package com.yellowbot.api;

/**
 * Signals internal exceptions.
 *
 * <p>
 * You should not see these exceptions. They are there
 * only to wrap exceptions which should not happen.
 * </p>
 *
 * <p>
 * Examples are:
 * <ul>
 *     <li>NoSuchAlgorithm exceptions because we could not find HMAC-SHA256 algorithm</li>
 *     <li>NoSuchEncoding exceptions because we could not find UTF-8 charset</li>
 *     <li>etc.</li>
 * </ul>
 * </p>
 *
 * <p>
 * Nevertheless, if these exceptions happen, it probably is:
 * <ul>
 *     <li>Because you have a problem in your Java platform (like using 1.4 rather than 1.6)</li>
 *     <li>Missing libraries</li>
 *     <li>Some bug that went overlooked</li>
 * </ul>
 * </p>
 *
 * <p>
 * Copyright 2011 Solfo Inc, all rights reserved.
 * </p>
 *
 * <!--
 * Created by IntelliJ IDEA.
 * Date: 10/18/11 1:33 PM
 * -->
 */
public class InternalException extends APIException {

     public InternalException() {
        super();
    }

    public InternalException( String s ) {
        super(s);
    }

    public InternalException( Throwable throwable ) {
        super(throwable);
    }

    public InternalException( String s, Throwable throwable ) {
         super(s, throwable);
    }
}
