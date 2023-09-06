package com.api.client.auth;

import com.api.client.assertion.AssertI;
import com.api.client.assertion.AssertionFactoryBuilder;

public class BasicAuthFactory implements AuthFactory<BasicAuth> {
    private static final AssertI assertThat = AssertionFactoryBuilder.buildTestNgAssertionFactory().get();
    private static final int EXPECTED_ARGS_LENGTH = 2;
    @Override
    public BasicAuth get(String... args) {
        assertThat.assertEquals(args.length, EXPECTED_ARGS_LENGTH, "Expected number of arguments not provided");
        // 0 - username, 1 - password
        return new BasicAuth(args[0],args[1]);
    }
}
