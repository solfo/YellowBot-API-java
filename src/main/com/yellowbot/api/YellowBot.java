package com.yellowbot.api;

import java.util.Map;

/**
 * The front door of this library.
 *
 * <p>
 *     See {@link Client} for details on usage.
 * </p>
 *
 * <p>
 * Copyright 2011 Solfo Inc, all rights reserved.
 * </p>
 *
 * <!--
 * Created by IntelliJ IDEA.
 * Date: 10/16/11 3:25 PM
 * -->
 */
public class YellowBot {

    public static Client newApiClient(String apiKey, char[] apiSecret) {
        return new ClientImpl(apiKey,  apiSecret);
    }

}
