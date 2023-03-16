package com.casper.sdk.model.deploy.executionresult;

import com.casper.sdk.model.deploy.ExecutionEffect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.*;

import java.math.BigInteger;
import java.util.List;

/**
 * Abstract Executable Result of type Failure containing the details of the
 * contract execution. It shows the result of a failed execution
 *
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @see ExecutionResult
 * @since 0.0.1
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonTypeName("Failure")
public class Failure implements ExecutionResult {

    /**
     * The cost of executing the deploy.
     */
    @JsonIgnore
    private BigInteger cost;

    /**
     * @see ExecutionEffect
     */
    private ExecutionEffect effect;

    /**
     * The error message associated with executing the deploy
     */
    @JsonProperty("error_message")
    private String errorMessage;

    /**
     * List of Hex-encoded transfer address.
     */
    private List<String> transfers;
}
