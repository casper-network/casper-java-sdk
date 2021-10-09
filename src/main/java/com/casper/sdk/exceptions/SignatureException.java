package com.casper.sdk.exceptions;

public class SignatureException extends RuntimeException {

    public SignatureException(final Throwable cause) {
        super(cause);
    }

    public SignatureException(final String message) {
        super(message);
    }
}
