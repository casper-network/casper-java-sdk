package com.syntifi.casper.sdk.exception;

import lombok.NoArgsConstructor;

/**
 * A simple exception to cover not implemented functionality
 *
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
@NoArgsConstructor
public class NotImplementedException extends RuntimeException {
    public NotImplementedException(String message) {
        super(message);
    }
}
