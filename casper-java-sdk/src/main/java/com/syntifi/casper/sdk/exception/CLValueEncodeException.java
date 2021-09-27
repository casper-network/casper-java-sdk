package com.syntifi.casper.sdk.exception;

import lombok.NoArgsConstructor;

/**
 * Thrown when CLValue could not be encoded
 *
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
@NoArgsConstructor
public class CLValueEncodeException extends Exception {
    public CLValueEncodeException(String message) {
        super(message);
    }

    public CLValueEncodeException(String message, Throwable cause) {
        super(message, cause);
    }
}
