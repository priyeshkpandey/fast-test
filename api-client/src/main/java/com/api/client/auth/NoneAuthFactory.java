package com.api.client.auth;

public class NoneAuthFactory implements AuthFactory<NoneAuth> {
    @Override
    public NoneAuth get(String... args) {
        return new NoneAuth();
    }
}
