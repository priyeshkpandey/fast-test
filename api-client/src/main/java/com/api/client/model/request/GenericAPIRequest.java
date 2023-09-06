package com.api.client.model.request;

import com.api.client.auth.AuthI;
import com.api.client.contract.APIRequest;
import com.api.client.contract.APIRequestBody;
import com.api.client.contract.HttpMethod;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Setter
@Getter
@Builder
public class GenericAPIRequest implements APIRequest {
    private String URL;
    private AuthI auth;
    private Map<String, String> headers;
    private Map<String, String> queryParams;
    private Map<String, String> pathParams;
    private HttpMethod httpMethod;
    private APIRequestBody body;
}
