package com.syntifi.casper.sdk.exception;

import java.io.IOException;

/**
 * Thrown in case of deserialization error
 *
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
public class DeserializationException extends IOException {
    public DeserializationException(String message) {
        super(message);
    }

    public DeserializationException(String message, Throwable cause) {
        super(message, cause);
    }

}
