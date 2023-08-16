package com.casper.sdk.e2e.exception;

/**
 * Exception thrown by e2e tests.
 *
 * @author ian@meywood.com
 */
public class TestException extends RuntimeException {

    public TestException(Throwable cause) {
        super(cause);
    }
}
