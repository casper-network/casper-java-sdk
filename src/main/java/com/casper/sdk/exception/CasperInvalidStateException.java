package com.casper.sdk.exception;

public class CasperInvalidStateException extends RuntimeException {

    public CasperInvalidStateException(String message, Throwable cause) {
        super(message, cause);
    }

    public CasperInvalidStateException(String message) {
        super(message);
    }
}
