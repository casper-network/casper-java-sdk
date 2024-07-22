package com.casper.sdk.model.transaction.execution;

import com.casper.sdk.model.deploy.executionresult.Failure;
import com.casper.sdk.model.deploy.executionresult.Success;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Version1 of an ExecutionResult
 *
 * @author ian@meywood.com
 */
@NoArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExecutionResultV1 extends ExecutionResult {

    @JsonProperty("Success")
    private Success success;
    @JsonProperty("Failure")
    private Failure failure;
}
