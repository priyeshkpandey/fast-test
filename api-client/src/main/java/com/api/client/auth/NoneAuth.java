package com.api.client.auth;

public class NoneAuth implements NoneAuthI {
    @Override
    public AuthType getType() {
        return AuthType.NONE;
    }
}
