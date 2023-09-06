package com.api.client.apache.factory;

import com.api.client.apache.ApacheClient;
import com.api.client.contract.APIClient;
import com.api.client.factory.APIClientFactoryI;
import com.api.client.interceptor.APIClientInterceptor;

import java.lang.reflect.Proxy;

public class ApacheAPIClientFactory implements APIClientFactoryI {
    @Override
    public APIClient get() {
        return (APIClient) Proxy.newProxyInstance(ApacheClient.class.getClassLoader(), new Class[]{APIClient.class},
                new APIClientInterceptor(new ApacheClient()));
    }
}
