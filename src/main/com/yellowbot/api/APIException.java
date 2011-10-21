package com.yellowbot.api;

/**
 * Signals errors thrown from {@link YellowBot} and {@link Client} methods.
 *
 * <p>
 * Notice these are unchecked exceptions, meaning
 * that the offending code which throws them
 * doesn't need to be surrounded by "try/catch"
 * constructions or to proliferate throws declaration
 * in the enclosing methods. Anyway, somewhere down
 * the line you want to catch them.
 * </p>
 *
 * <p>
 * Copyright 2011 Solfo Inc, all rights reserved.
 * </p>
 *
 * <!--
 * Created by IntelliJ IDEA.
 * Date: 10/18/11 1:28 PM
 * -->
 */
public class APIException extends RuntimeException {

    public APIException() {
        super();
    }

    public APIException( String s ) {
        super(s);
    }

    public APIException( Throwable throwable ) {
        super(throwable);
    }

    public APIException( String s, Throwable throwable ) {
         super(s, throwable);
    }
}
