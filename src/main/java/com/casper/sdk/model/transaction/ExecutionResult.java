package com.casper.sdk.model.transaction;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Abstract class for execution results.
 *
 * @author ian@meywood.com
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
@JsonSubTypes({
        @JsonSubTypes.Type(value = ExecutionResultV1.class, name = "Version1"),
        @JsonSubTypes.Type(value = ExecutionResultV2.class, name = "Version2")})
@NoArgsConstructor
@Getter
@Setter
public abstract class ExecutionResult {

}
