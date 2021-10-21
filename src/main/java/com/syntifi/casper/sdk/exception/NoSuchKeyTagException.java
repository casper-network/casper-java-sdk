package com.syntifi.casper.sdk.exception;

import lombok.NoArgsConstructor;

/**
 * Thrown in case of a Key tag which does not exist being requested
 *
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
@NoArgsConstructor
public class NoSuchKeyTagException extends Exception {
    public NoSuchKeyTagException(String message) {
        super(message);
    }

    public NoSuchKeyTagException(String message, Throwable cause) {
        super(message, cause);
    }
}
