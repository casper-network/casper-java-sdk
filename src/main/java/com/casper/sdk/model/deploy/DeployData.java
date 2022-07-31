package com.casper.sdk.model.deploy;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Root class for a Casper deploy request
 *
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeployData {

    /**
     * The RPC API version
     */
    @JsonProperty("api_version")
    private String apiVersion;

    /**
     * the {@link Deploy}
     */
    @JsonProperty("deploy")
    private Deploy deploy;

    /**
     * a list of {@link JsonExecutionResult}
     */
    @JsonProperty("execution_results")
    private List<JsonExecutionResult> executionResults;
}
