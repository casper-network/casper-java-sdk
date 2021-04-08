package com.casper.sdk.exceptions;

public class ValueNotFoundException extends Exception {

    public ValueNotFoundException(final String message) {
        super(message);
    }
    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
