package com.api.client.contract;

public interface APIClient {
    public APIResponse execute(final APIRequest request);
}
