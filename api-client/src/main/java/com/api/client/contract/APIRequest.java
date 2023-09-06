package com.api.client.contract;

import com.api.client.auth.AuthI;

import java.util.Map;

public interface APIRequest {
    public String getURL();
    public AuthI getAuth();
    public Map<String, String> getHeaders();
    public Map<String, String> getQueryParams();
    public Map<String, String> getPathParams();
    public HttpMethod getHttpMethod();
    public APIRequestBody getBody();
}
