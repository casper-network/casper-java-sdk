package com.syntifi.casper.sdk.exception;

import lombok.NoArgsConstructor;

/**
 * Thrown in case of a CLType which does not exist being requested
 *
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
@NoArgsConstructor
public class NoSuchCLTypeException extends Exception {
    public NoSuchCLTypeException(String message) {
        super(message);
    }

    public NoSuchCLTypeException(String message, Throwable cause) {
        super(message, cause);
    }
}
