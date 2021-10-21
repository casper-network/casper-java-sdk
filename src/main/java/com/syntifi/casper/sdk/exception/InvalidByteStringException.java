package com.syntifi.casper.sdk.exception;

import lombok.NoArgsConstructor;

/**
 * Thrown in case of an invalid (unparseable) encoded byte string
 *
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
@NoArgsConstructor
public class InvalidByteStringException extends CLValueDecodeException {
    public InvalidByteStringException(String message) {
        super(message);
    }

    public InvalidByteStringException(String message, Throwable cause) {
        super(message, cause);
    }
}
