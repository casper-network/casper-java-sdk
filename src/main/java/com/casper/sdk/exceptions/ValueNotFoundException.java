package com.casper.sdk.exceptions;

/**
 * Custom exception to throw when json key not found
 */
public class ValueNotFoundException extends Exception {

    public ValueNotFoundException(final String message) {
        super(message);
    }
    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
