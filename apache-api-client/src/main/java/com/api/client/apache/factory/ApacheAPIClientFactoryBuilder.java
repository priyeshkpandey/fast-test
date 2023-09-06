package com.api.client.apache.factory;

import com.api.client.factory.APIClientFactoryI;

public class ApacheAPIClientFactoryBuilder {
    private static ApacheAPIClientFactory apacheAPIClientFactory;

    public static APIClientFactoryI getApacheAPIClientFactory() {
        if (null == apacheAPIClientFactory) {
            apacheAPIClientFactory = new ApacheAPIClientFactory();
        }
        return apacheAPIClientFactory;
    }
}
