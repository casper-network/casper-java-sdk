package com.casper.sdk.model.deploy;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

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
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<JsonExecutionResult> executionResults;
}
