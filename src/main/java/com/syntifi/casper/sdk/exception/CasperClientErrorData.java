package com.syntifi.casper.sdk.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Json RPC service error data
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
@Getter
@Setter
@Builder
public class CasperClientErrorData {
    private int code;
    private String message;
    private Object data;
}
