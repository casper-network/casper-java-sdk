package com.syntifi.casper.sdk.exception;

/**
 * Thrown on casper RPC service error
 *
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
public class CasperClientException extends RuntimeException {

    public CasperClientException(CasperClientErrorData error) {
        super(String.format("%s (code: %d)", error.getMessage(), error.getCode()));
    }

    public CasperClientException(String message, Throwable cause) {
        super(message, cause);
    }
}
