package com.syntifi.casper.sdk.exception;

import lombok.Data;

/**
 * Json RPC service error data
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
@Data
public class CasperClientErrorData {
    private int code;
    private String message;
    private Object data;
}
