package com.syntifi.casper.sdk.exception;

import java.io.IOException;

/**
 * Thrown in case of serialization error
 *
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
public class SerializationException extends IOException {
    public SerializationException(String message) {
        super(message);
    }

    public SerializationException(String message, Throwable cause) {
        super(message, cause);
    }
}
