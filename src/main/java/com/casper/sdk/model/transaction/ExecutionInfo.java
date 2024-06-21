package com.casper.sdk.model.transaction;

import com.casper.sdk.model.common.Digest;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;

/**
 * @author ian@meywood.com
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ExecutionInfo {
    @JsonProperty("block_hash")
    private Digest blockHash;
    @JsonProperty("block_height")
    private BigInteger blockHeight;
    @JsonProperty("execution_result")
    private ExecutionResult executionResult;

    public <T extends ExecutionResult> T getExecutionResult() {
        //noinspection unchecked
        return (T)executionResult;
    }
}
