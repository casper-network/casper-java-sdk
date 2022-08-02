package com.casper.sdk.model.event.deployprocessed;

import com.casper.sdk.model.deploy.executionresult.ExecutionResult;
import com.casper.sdk.model.event.EventData;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.*;

import java.util.List;

/**
 * The DeployProcessed event is emitted when a given Deploy has been executed.
 * 
 * @author ian@meywood.com 
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonTypeName("DeployProcessed")
public class DeployProcessed implements EventData {

    @JsonProperty("deploy_hash")
    private String deployHash;

    @JsonProperty("account")
    private String account;

    @JsonProperty("timestamp")
    private String timestamp; // TODO convert to data

    @JsonProperty("ttl")
    private String ttl;

    @JsonProperty("dependencies")
    private List<String> dependencies;

    @JsonProperty("block_hash")
    private String blockHash;

    @JsonProperty("execution_result")
    private ExecutionResult executionResult;
}
