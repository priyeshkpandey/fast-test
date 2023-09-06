package com.api.client.auth;

public class CertAuth implements CertAuthI {

    protected String keystorePath;
    protected String keystorePassword;
    protected String keystoreType;

    public CertAuth(String keyStorePath, String password, String keystoreType) {
        this.keystorePath = keyStorePath;
        this.keystorePassword = password;
        this.keystoreType = keystoreType;
    }

    public AuthType getType() {
        return AuthType.CERT;
    }

    @Override
    public String getKeyStorePath() {
        return this.keystorePath;
    }

    @Override
    public String getKeyStorePassword() {
        return this.keystorePassword;
    }

    @Override
    public String getKeyStoreType() {
        return this.keystoreType;
    }
}
