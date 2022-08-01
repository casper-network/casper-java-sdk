package com.casper.sdk.exception;

import lombok.*;

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
@NoArgsConstructor
@AllArgsConstructor
public class CasperClientErrorData {
    private int code;
    private String message;
    private Object data;
}
