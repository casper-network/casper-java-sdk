package com.casper.sdk.exceptions;

public class HttpException extends Exception {

    public HttpException(final String message) {
        super(message);
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }

}
