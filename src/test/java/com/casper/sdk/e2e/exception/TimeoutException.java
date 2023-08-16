package com.casper.sdk.e2e.exception;

/**
 * @author ian@meywood.com
 */
public class TimeoutException extends RuntimeException {

    public TimeoutException(final String message) {
        super(message);
    }
}
