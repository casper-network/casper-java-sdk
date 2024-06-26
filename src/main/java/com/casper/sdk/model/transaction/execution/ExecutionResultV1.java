package com.casper.sdk.model.transaction.execution;

import com.casper.sdk.model.deploy.executionresult.Failure;
import com.casper.sdk.model.deploy.executionresult.Success;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Version1 of an ExecutionResult
 *
 * @author ian@meywood.com
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Failure.class, name = "Failure"),
        @JsonSubTypes.Type(value = Success.class, name = "Success")})
@NoArgsConstructor
@Getter
@Setter
public class ExecutionResultV1 extends ExecutionResult {
}
