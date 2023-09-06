package com.api.client.auth;

public interface BasicAuthI extends AuthI {
    public String getUsername();
    public String getPassword();
}
