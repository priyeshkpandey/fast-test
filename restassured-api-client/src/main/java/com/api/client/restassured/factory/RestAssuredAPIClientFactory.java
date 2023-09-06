package com.api.client.restassured.factory;

import com.api.client.contract.APIClient;
import com.api.client.factory.APIClientFactoryI;
import com.api.client.interceptor.APIClientInterceptor;
import com.api.client.restassured.RestAssuredClient;

import java.lang.reflect.Proxy;

public class RestAssuredAPIClientFactory implements APIClientFactoryI {
    @Override
    public APIClient get() {
        return (APIClient) Proxy.newProxyInstance(RestAssuredClient.class.getClassLoader(), new Class[]{APIClient.class},
                new APIClientInterceptor(new RestAssuredClient()));
    }
}
