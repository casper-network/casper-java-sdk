package com.syntifi.casper.sdk.model.deploy;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.syntifi.casper.sdk.model.deploy.executionresult.ExecutionResult;

import lombok.Data;

/**
 * The execution result of a single deploy.
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
@Data
public class JsonExecutionResult {
    
     /**
     * The block hash.
     */
    @JsonProperty("block_hash")
    private String blockHash;

    /**
     * @see ExecutionResult
     */
    private ExecutionResult result;
}

