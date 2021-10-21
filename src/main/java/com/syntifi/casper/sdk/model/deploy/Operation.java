package com.syntifi.casper.sdk.model.deploy;

import lombok.Data;

/**
 * An operation performed while executing a deploy.
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
@Data
public class Operation {
    
    /**
     * The formatted string of the `Key`
     */
    private String key;

    /**
     * @see OpKind 
     */
    private OpKind kind;
}
