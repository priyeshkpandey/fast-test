package com.api.client.assertion;

public interface AssertI<T> {
    public void assertNotNull(final Object object, final String message);
    public void fail(final String message, final Throwable throwable);
    public void assertEquals(final Object actual, final Object expected, final String message);
    public void assertTrue(final Boolean condition, final String message);
    public void assertFalse(final Boolean condition, final String message);
}
