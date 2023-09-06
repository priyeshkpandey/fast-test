package com.api.client.restassured;

import com.api.client.auth.AuthI;
import com.api.client.auth.BasicAuth;
import com.api.client.auth.CertAuth;
import com.api.client.contract.*;
import com.api.client.model.response.GenericAPIResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.restassured.RestAssured;
import io.restassured.config.EncoderConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;

import static io.restassured.RestAssured.given;

public class RestAssuredClient implements APIClient {
    private static final Logger LOG = LoggerFactory.getLogger(RestAssuredClient.class);
    private RestAssuredConfig config;
    private RequestSpecification requestSpecification;

    @Override
    public APIResponse execute(final APIRequest request) {
        LOG.debug("Executing URL: {}, method: {}, body: {}, headers: {}, params: {}",
                request.getURL(), request.getHttpMethod(), request.getBody(), request.getHeaders(), request.getQueryParams());
        HttpMethod httpMethod = request.getHttpMethod();
        Map<String, String> queryParams = request.getQueryParams();
        Map<String, String> pathParams = request.getPathParams();
        LOG.debug("{} request to {}", httpMethod, request.getURL());
        if (request.getHeaders() != null) LOG.debug("Headers: {}", request.getHeaders());
        if (httpMethod == HttpMethod.GET || httpMethod == HttpMethod.DELETE) {
            if (queryParams != null) LOG.debug("Query parameters: {}", queryParams);
            if (pathParams != null) LOG.debug("Path parameters: {}", pathParams);
        } else {
            try {
                LOG.debug("Request body:\n {}", new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT).writeValueAsString(request.getBody().getObject()));
            } catch (JsonProcessingException e) {
                LOG.debug("Could not log the request body"); // logging to debug instead of error since we do not want the test to fail because of this
                LOG.debug(e.getMessage());
            }
        }
        return prepareAndExecute(request);
    }

    private APIResponse prepareAndExecute(final APIRequest request) {
        init();
        configAuth(request.getAuth());
        setUrlEncoding(false);
        addHeaders(request.getHeaders());
        addQueryParams(request.getQueryParams());
        addPathParams(request.getPathParams());
        addBody(request.getBody());
        Response restAssuredResponse = null;
        final Instant start = Instant.now();
        switch (request.getHttpMethod()) {
            case GET:
                restAssuredResponse = requestSpecification.get(request.getURL());
                break;
            case PUT:
                restAssuredResponse = requestSpecification.put(request.getURL());
                break;
            case POST:
                restAssuredResponse = requestSpecification.post(request.getURL());
                break;
            case DELETE:
                restAssuredResponse = requestSpecification.delete(request.getURL());
                break;
            case PATCH:
                restAssuredResponse = requestSpecification.patch(request.getURL());
                break;
        }
        APIResponse response = GenericAPIResponse.builder()
                .status(restAssuredResponse.getStatusLine())
                .statusCode(String.valueOf(restAssuredResponse.getStatusCode()))
                .object(restAssuredResponse.body().prettyPrint())
                .build();
        LOG.debug("Response body:\n {}", restAssuredResponse.getBody().prettyPrint());
        return response;
    }

    private void init() {
        this.config = RestAssured.config()
                .encoderConfig(new EncoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false));
    }

    private void configAuth(final AuthI auth) {
        switch (auth.getType()) {
            case NONE:
                requestSpecification = given().request();
                break;
            case BASIC:
                final BasicAuth basicAuth = (BasicAuth) auth;
                requestSpecification = given().request();
                requestSpecification.auth().basic(basicAuth.getUsername(), basicAuth.getPassword());
                break;
            case CERT:
                final CertAuth certAuth = (CertAuth) auth;
                config = config.sslConfig(config.getSSLConfig().with()
                        .keyStore(certAuth.getKeyStorePath(), certAuth.getKeyStorePassword()).and()
                        .allowAllHostnames());
                requestSpecification = given().request();
                break;
        }
    }

    private void setUrlEncoding(final boolean isUrlEncodingEnabled) {
        requestSpecification.urlEncodingEnabled(isUrlEncodingEnabled);
    }

    private void addHeaders(final Map<String, String> headers) {
        if (null != headers) {
            requestSpecification.headers(headers);
        }
    }

    private void addQueryParams(final Map<String, String> queryParams) {
        if (null != queryParams) {
            requestSpecification.queryParams(queryParams);
        }
    }

    private void addPathParams(final Map<String, String> pathParams) {
        if (null != pathParams) {
            requestSpecification.pathParams(pathParams);
        }
    }

    private void addBody(final APIRequestBody requestBody) {
        if (requestBody.getType().equals(RequestBodyType.MULTIPART)) {
            Optional.ofNullable(requestBody.getMultipartBodyList()).ifPresent(multipart -> {
                multipart.forEach(mp -> {
                    if (mp.getContentBody() != null) {
                        requestSpecification.multiPart(mp.getControlName(), mp.getContentBody());
                    }
                    if (mp.getFile() != null) {
                        requestSpecification.multiPart(mp.getControlName(), mp.getFile(), mp.getMimeType());
                    }
                });
            });
        }
        if (null != requestBody.getObject()) {
            requestSpecification.body(requestBody.getObject());
        }

    }

}
