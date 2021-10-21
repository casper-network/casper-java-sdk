package com.syntifi.casper.sdk.exception;

import lombok.NoArgsConstructor;

/**
 * Thrown when CLValue could not be decoded
 *
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
@NoArgsConstructor
public class CLValueDecodeException extends Exception {
    public CLValueDecodeException(String message) {
        super(message);
    }

    public CLValueDecodeException(String message, Throwable cause) {
        super(message, cause);
    }
}
