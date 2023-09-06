package com.api.client.auth;

public class AuthFactoryBuilder {
    private static CertAuthFactory certAuthFactory;
    private static BasicAuthFactory basicAuthFactory;
    private static NoneAuthFactory noneAuthFactory;

    public static CertAuthFactory getCertAuthFactory() {
        if (null == certAuthFactory) {
            certAuthFactory = new CertAuthFactory();
        }
        return certAuthFactory;
    }

    public static BasicAuthFactory getBasicAuthFactory() {
        if (null == basicAuthFactory) {
            basicAuthFactory = new BasicAuthFactory();
        }
        return basicAuthFactory;
    }

    public static NoneAuthFactory getNoneAuthFactory() {
        if (null == noneAuthFactory) {
            noneAuthFactory = new NoneAuthFactory();
        }
        return noneAuthFactory;
    }
}
