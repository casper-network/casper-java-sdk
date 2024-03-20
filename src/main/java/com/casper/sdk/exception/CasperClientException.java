package com.casper.sdk.exception;

/**
 * Thrown on casper RPC service error
 *
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
public class CasperClientException extends RuntimeException {

    public CasperClientException(final CasperClientErrorData error) {
        super(String.format("%s (code: %d) %s", error.getMessage(), error.getCode(), error.getData() != null ? error.getData() : ""));
    }

    public CasperClientException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public CasperClientException(final String message) {
        super(message);
    }
}
