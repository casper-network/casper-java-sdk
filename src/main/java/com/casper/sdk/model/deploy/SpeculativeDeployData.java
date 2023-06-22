package com.casper.sdk.model.deploy;

import com.casper.sdk.model.deploy.executionresult.ExecutionResult;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

/**
 * Root class for a Casper deploy request
 *
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SpeculativeDeployData {

    /**
     * The RPC API version
     */
    @JsonProperty("api_version")
    private String apiVersion;

    /**
     * The block hash.
     */
    @JsonProperty("block_hash")
    private String blockHash;

    /**
     * @see ExecutionResult
     */
    @JsonProperty("execution_result")
    private ExecutionResult executionResult;
}
