package com.test.api.client.restassured;

import com.api.client.auth.AuthFactoryBuilder;
import com.api.client.contract.*;
import com.api.client.model.request.GenericAPIRequest;
import com.api.client.model.request.GenericAPIRequestBody;
import com.api.client.restassured.factory.RestAssuredAPIClientFactoryBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.util.model.UserSignUpRequest;
import com.util.model.UserSignUpResponse;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.Reporter;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static com.util.constant.Endpoint.ShoppingEndpoint.SEARCH;
import static com.util.constant.Endpoint.UserEndpoint.USER_SIGNUP;

public class MockServiceRestAssuredClientTest {
    private static final Random random = new Random();
    private static final ObjectMapper mapper = new ObjectMapper();

    private APIClient client;
    private String baseUrl;

    @BeforeTest(groups = {"restassured"})
    public void beforeTestSetup(final ITestContext context) {
        context.setAttribute("client", RestAssuredAPIClientFactoryBuilder.getRestAssuredAPIClientFactory().get());
        context.setAttribute("base_url", "http://localhost:11000");
    }

    @Test(groups = {"restassured"}, invocationCount = 60)
    public void searchProduct(final ITestContext context) {
        this.client = (APIClient) context.getAttribute("client");
        this.baseUrl = (String) context.getAttribute("base_url");
        final APIRequestBody requestBody = GenericAPIRequestBody.builder().type(RequestBodyType.NONE).build();
        final Map<String, String> queryParams = new HashMap<>();
        queryParams.put("q", "Pen");
        final String url = this.baseUrl + SEARCH;
        final APIRequest request = GenericAPIRequest.builder()
                .URL(url).httpMethod(HttpMethod.GET)
                .body(requestBody).auth(AuthFactoryBuilder.getNoneAuthFactory().get())
                .queryParams(queryParams).build();
        final APIResponse response = this.client.execute(request);
        Assert.assertEquals(response.getStatusCode(), "200");
        Reporter.log(response.getObject().toString());
    }

    @Test(groups = {"restassured"}, invocationCount = 10)
    public void signUpAndSearch(final ITestContext context) throws JsonProcessingException {
        this.client = (APIClient) context.getAttribute("client");
        this.baseUrl = (String) context.getAttribute("base_url");

        final UserSignUpRequest userSignUpRequest = new UserSignUpRequest();
        final long expectedUserId = random.nextLong();
        userSignUpRequest.setId(expectedUserId);
        final long currentTimeMillis = System.currentTimeMillis();
        final String expectedFirstName = "Customer_First_Name_" + currentTimeMillis;
        final String expectedLastName = "Customer_Last_Name_" + currentTimeMillis;
        final String userName = "customer_username_" + currentTimeMillis;
        final String password = "cust_password";
        userSignUpRequest.setUserName(userName);
        userSignUpRequest.setPassword(password);
        userSignUpRequest.setFirstName(expectedFirstName);
        userSignUpRequest.setLastName(expectedLastName);
        final APIRequestBody signUpRequestBody = GenericAPIRequestBody.builder().type(RequestBodyType.OBJECT)
                .object(userSignUpRequest).build();
        final String signUpUrl = this.baseUrl + USER_SIGNUP;
        final Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        final APIRequest signUpRequest = GenericAPIRequest.builder().URL(signUpUrl).httpMethod(HttpMethod.POST)
                .body(signUpRequestBody).auth(AuthFactoryBuilder.getNoneAuthFactory().get())
                .headers(headers).build();
        final APIResponse signUpResponse = this.client.execute(signUpRequest);
        Assert.assertEquals(signUpResponse.getStatusCode(), "200");
        Reporter.log(signUpResponse.getObject().toString());
        final UserSignUpResponse userSignUpResponse = mapper.readValue(signUpResponse.getObject().toString(),
                UserSignUpResponse.class);
        Assert.assertEquals(userSignUpResponse.getRoles().get(0), "customer");
        Assert.assertEquals(userSignUpResponse.getPermissions().get(0).getName(), "customer");

        final APIRequestBody searchRequestBody = GenericAPIRequestBody.builder().type(RequestBodyType.NONE).build();
        final Map<String, String> queryParams = new HashMap<>();
        queryParams.put("q", "Pen");
        final String searchUrl = this.baseUrl + SEARCH;
        final APIRequest searchRequest = GenericAPIRequest.builder()
                .URL(searchUrl).httpMethod(HttpMethod.GET)
                .body(searchRequestBody).auth(AuthFactoryBuilder.getNoneAuthFactory().get())
                .queryParams(queryParams).build();
        final APIResponse searchResponse = this.client.execute(searchRequest);
        Assert.assertEquals(searchResponse.getStatusCode(), "200");
        Reporter.log(searchResponse.getObject().toString());
    }

    @Test(groups = {"restassured"}, invocationCount = 10)
    public void signUpAndSearchWithNewClient(final ITestContext context) throws JsonProcessingException {
        this.client = RestAssuredAPIClientFactoryBuilder.getRestAssuredAPIClientFactory().get();
        this.baseUrl = (String) context.getAttribute("base_url");

        final UserSignUpRequest userSignUpRequest = new UserSignUpRequest();
        final long expectedUserId = random.nextLong();
        userSignUpRequest.setId(expectedUserId);
        final long currentTimeMillis = System.currentTimeMillis();
        final String expectedFirstName = "Customer_First_Name_" + currentTimeMillis;
        final String expectedLastName = "Customer_Last_Name_" + currentTimeMillis;
        final String userName = "customer_username_" + currentTimeMillis;
        final String password = "cust_password";
        userSignUpRequest.setUserName(userName);
        userSignUpRequest.setPassword(password);
        userSignUpRequest.setFirstName(expectedFirstName);
        userSignUpRequest.setLastName(expectedLastName);
        final APIRequestBody signUpRequestBody = GenericAPIRequestBody.builder().type(RequestBodyType.OBJECT)
                .object(userSignUpRequest).build();
        final String signUpUrl = this.baseUrl + USER_SIGNUP;
        final Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        final APIRequest signUpRequest = GenericAPIRequest.builder().URL(signUpUrl).httpMethod(HttpMethod.POST)
                .body(signUpRequestBody).auth(AuthFactoryBuilder.getNoneAuthFactory().get())
                .headers(headers).build();
        final APIResponse signUpResponse = this.client.execute(signUpRequest);
        Assert.assertEquals(signUpResponse.getStatusCode(), "200");
        Reporter.log(signUpResponse.getObject().toString());
        final UserSignUpResponse userSignUpResponse = mapper.readValue(signUpResponse.getObject().toString(),
                UserSignUpResponse.class);
        Assert.assertEquals(userSignUpResponse.getRoles().get(0), "customer");
        Assert.assertEquals(userSignUpResponse.getPermissions().get(0).getName(), "customer");

        final APIRequestBody searchRequestBody = GenericAPIRequestBody.builder().type(RequestBodyType.NONE).build();
        final Map<String, String> queryParams = new HashMap<>();
        queryParams.put("q", "Pen");
        final String searchUrl = this.baseUrl + SEARCH;
        final APIRequest searchRequest = GenericAPIRequest.builder()
                .URL(searchUrl).httpMethod(HttpMethod.GET)
                .body(searchRequestBody).auth(AuthFactoryBuilder.getNoneAuthFactory().get())
                .queryParams(queryParams).build();
        final APIResponse searchResponse = this.client.execute(searchRequest);
        Assert.assertEquals(searchResponse.getStatusCode(), "200");
        Reporter.log(searchResponse.getObject().toString());
    }
}
