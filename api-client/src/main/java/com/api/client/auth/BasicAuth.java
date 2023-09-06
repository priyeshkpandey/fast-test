package com.api.client.auth;

public class BasicAuth implements BasicAuthI {
    private String username;
    private String password;

    public BasicAuth(final String username, final String password) {
        this.username = username;
        this.password = password;
    }
    @Override
    public AuthType getType() {
        return AuthType.BASIC;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public String getPassword() {
        return this.password;
    }
}
