package com.api.client.apache;

import com.api.client.auth.AuthI;
import com.api.client.auth.BasicAuth;
import com.api.client.auth.CertAuth;
import com.api.client.contract.*;
import com.api.client.model.response.GenericAPIResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.http.HttpEntity;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import java.io.*;
import java.security.KeyStore;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class ApacheClient implements APIClient {
    private static final Logger LOG = LoggerFactory.getLogger(ApacheClient.class);

    private CloseableHttpClient httpClient;
    private HttpClientBuilder httpClientBuilder;
    private HttpUriRequest httpRequest;
    private String url;
    private HttpEntity entity;

    @Override
    public APIResponse execute(APIRequest request) {
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
        url = request.getURL();
        init();
        configAuth(request.getAuth());
        addQueryParams(request.getQueryParams());
        addPathParams(request.getPathParams());
        addBody(request.getBody());
        switch (request.getHttpMethod()) {
            case GET:
                httpRequest = new HttpGet(url);
                break;
            case PUT:
                httpRequest = new HttpPut(url);
                if (null != entity) {
                    ((HttpPut)httpRequest).setEntity(entity);
                }
                break;
            case POST:
                httpRequest = new HttpPost(url);
                if (null != entity) {
                    ((HttpPost)httpRequest).setEntity(entity);
                }
                break;
            case DELETE:
                httpRequest = new HttpDelete(url);
                break;
            case PATCH:
                httpRequest = new HttpPatch(url);
                if (null != entity) {
                    ((HttpPatch)httpRequest).setEntity(entity);
                }
                break;
        }
        addHeaders(request.getHeaders());
        httpClient = httpClientBuilder.build();
        CloseableHttpResponse response = null;
        APIResponse apiResponse = null;
        try {
            response = httpClient.execute(httpRequest);
            if (null != response) {
                try {
                    apiResponse = GenericAPIResponse.builder()
                            .status(response.getStatusLine().getReasonPhrase())
                            .statusCode(String.valueOf(response.getStatusLine().getStatusCode()))
                            .object(EntityUtils.toString(response.getEntity()))
                            .build();
                    LOG.info(apiResponse.getObject().toString());
                } catch (IOException e) {
                    LOG.error("Exception while extracting response. ", e);
                    apiResponse = GenericAPIResponse.builder().status("ERROR").statusCode(e.getMessage()).build();
                }
            } else {
                apiResponse = GenericAPIResponse.builder().status("NULL").statusCode("NULL").build();
            }
        } catch (IOException e) {
            LOG.error("Exception in execution. ", e);
        } finally {
            if (null != response) {
                EntityUtils.consumeQuietly(response.getEntity());
                try {
                    response.close();
                    httpClient.close();
                } catch (IOException e) {
                    LOG.error("Exception closing the response. ", e);
                }

            }

        }
        return apiResponse;
    }

    private void init() {
        if (null == httpClientBuilder) {
            httpClientBuilder = HttpClientBuilder.create();
        }
    }

    private void configAuth(final AuthI auth) {
        switch (auth.getType()) {
            case NONE:
                break;
            case BASIC:
                final BasicAuth basicAuth = (BasicAuth) auth;
                CredentialsProvider provider = new BasicCredentialsProvider();
                UsernamePasswordCredentials credentials
                        = new UsernamePasswordCredentials(basicAuth.getUsername(), basicAuth.getPassword());
                provider.setCredentials(AuthScope.ANY, credentials);
                httpClientBuilder.setDefaultCredentialsProvider(provider);
                break;
            case CERT:
                final CertAuth certAuth = (CertAuth) auth;
                try {
                    SSLContext sslContext = SSLContexts.custom()
                            .loadKeyMaterial(readStore(certAuth.getKeyStorePath(), certAuth.getKeyStorePassword()), null)
                            .build();
                    httpClientBuilder.setSSLContext(sslContext);
                } catch (Exception e) {
                    LOG.error("Exception while initializing cert auth. ", e);
                }
                break;
        }
    }

    private KeyStore readStore(final String keyStorePath, final String keyStorePass) throws Exception {
        try (InputStream keyStoreStream = this.getClass().getResourceAsStream(keyStorePath)) {
            KeyStore keyStore = KeyStore.getInstance("JKS"); // or "PKCS12"
            keyStore.load(keyStoreStream, keyStorePass.toCharArray());
            return keyStore;
        }
    }

    private void addHeaders(final Map<String, String> headers) {
        if (null != headers) {
            for (String headerKey : headers.keySet()) {
                httpRequest.addHeader(headerKey, headers.get(headerKey));
            }
        }
    }

    private void addQueryParams(final Map<String, String> queryParams) {
        if (null != queryParams) {
            final StringBuffer queryParamsSb = new StringBuffer(url + "?");
            for (String queryKey : queryParams.keySet()) {
                queryParamsSb.append(queryKey).append("=").append(queryParams.get(queryKey)).append("&");
            }
            url = queryParamsSb.toString().substring(0, queryParamsSb.lastIndexOf("&"));
        }
    }

    private void addPathParams(final Map<String, String> pathParams) {
        if (null != pathParams) {
            for (String pathParamKey: pathParams.keySet()) {
                url = url.replace("{" + pathParamKey + "}", pathParams.get(pathParamKey));
            }
        }
    }

    private void addBody(final APIRequestBody requestBody) {
        if (requestBody.getType().equals(RequestBodyType.MULTIPART)) {
            final MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
            Optional.ofNullable(requestBody.getMultipartBodyList()).ifPresent(multipart -> {
                multipart.forEach(mp -> {
                    if (mp.getContentBody() != null) {
                        multipartEntityBuilder.addTextBody(mp.getControlName(), mp.getContentBody());
                    }
                    if (mp.getFile() != null) {
                        multipartEntityBuilder.addBinaryBody(mp.getControlName(), mp.getFile());
                    }
                });
            });
            entity = multipartEntityBuilder.build();
        }
        if (null != requestBody.getObject()) {
            try {
                entity = new StringEntity(new ObjectMapper().writeValueAsString(requestBody.getObject()));
                ((StringEntity)(entity)).setContentType("application/json");
            } catch (UnsupportedEncodingException | JsonProcessingException e) {
                LOG.error("Exception creating entity. ", e);
            }
        }

    }
}
