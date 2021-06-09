package com.casper.sdk.exceptions;

public class ConversionException extends RuntimeException {

    public ConversionException(Throwable cause) {
        super(cause);
    }

    public ConversionException(final String message) {
        super(message);
    }

    public ConversionException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
