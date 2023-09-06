package com.api.client.assertion;

public class TestNgAssertionFactory implements AssertionFactory<TestNgAssert> {
    private static TestNgAssert testNgAssert;
    @Override
    public TestNgAssert get() {
        if (null == testNgAssert) {
            testNgAssert = new TestNgAssert();
        }
        return testNgAssert;
    }
}
