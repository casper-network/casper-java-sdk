package com.syntifi.casper.sdk.exception;

/**
 * Thrown when trying to read a value when all bytes were read
 *
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
public class BufferEndCLValueDecodeException extends CLValueDecodeException {
    public BufferEndCLValueDecodeException(String message) {
        super(message);
    }

    public BufferEndCLValueDecodeException(String message, Throwable cause) {
        super(message, cause);
    }    
}
