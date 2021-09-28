package com.casper.sdk.exceptions;

public class SignatureException extends RuntimeException {

    public SignatureException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public SignatureException(final Throwable cause) {
        super(cause);
    }
}
