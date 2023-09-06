package com.api.client.assertion;

import org.testng.Assert;

public class TestNgAssert extends AbstractTestNgAssertion {
    @Override
    public void assertNotNull(Object object, String message) {
        Assert.assertNotNull(object, message);
    }

    @Override
    public void fail(String message, Throwable throwable) {
        Assert.fail(message, throwable);
    }

    @Override
    public void assertEquals(Object actual, Object expected, String message) {
        Assert.assertEquals(actual, expected, message);
    }

    @Override
    public void assertTrue(Boolean condition, String message) {
        Assert.assertTrue(condition, message);
    }

    @Override
    public void assertFalse(Boolean condition, String message) {
        Assert.assertFalse(condition, message);
    }
}
