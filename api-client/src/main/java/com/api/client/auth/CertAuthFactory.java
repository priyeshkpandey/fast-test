package com.api.client.auth;

import com.api.client.assertion.AssertI;
import com.api.client.assertion.AssertionFactoryBuilder;

public class CertAuthFactory implements AuthFactory<CertAuth> {
    private static final AssertI assertThat = AssertionFactoryBuilder.buildTestNgAssertionFactory().get();
    private static final int EXPECTED_ARGS_LENGTH = 3;
    @Override
    public CertAuth get(final String... args) {
        assertThat.assertEquals(args.length, EXPECTED_ARGS_LENGTH, "Expected number of arguments not provided");
        // 0 - keystore path, 1 - keystore password, 2 - keystore type
        return new CertAuth(args[0],args[1], args[2]);
    }
}
