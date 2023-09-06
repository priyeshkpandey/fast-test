package com.api.client.auth;

public interface CertAuthI extends AuthI {
    public String getKeyStorePath();
    public String getKeyStorePassword();
    public String getKeyStoreType();
}
