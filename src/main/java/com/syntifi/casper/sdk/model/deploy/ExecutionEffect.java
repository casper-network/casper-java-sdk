package com.syntifi.casper.sdk.model.deploy;

import java.util.List;

import lombok.Data;

/**
 * The effect of executing a single deploy
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
@Data
public class ExecutionEffect {
    
    /**
     * @see Operation
     */
    private List<Operation> operations;

    /**
     * @see TrasnformEntry 
     */
    private List<Entry> transforms;
}
