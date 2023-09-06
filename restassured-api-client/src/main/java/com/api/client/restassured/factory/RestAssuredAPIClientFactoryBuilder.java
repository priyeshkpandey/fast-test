package com.api.client.restassured.factory;

import com.api.client.factory.APIClientFactoryI;

public class RestAssuredAPIClientFactoryBuilder {
    private static RestAssuredAPIClientFactory restAssuredAPIClientFactory;

    public static APIClientFactoryI getRestAssuredAPIClientFactory() {
        if (null == restAssuredAPIClientFactory) {
            restAssuredAPIClientFactory = new RestAssuredAPIClientFactory();
        }
        return restAssuredAPIClientFactory;
    }
}
