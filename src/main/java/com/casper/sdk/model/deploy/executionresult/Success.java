package com.casper.sdk.model.deploy.executionresult;

import com.casper.sdk.model.deploy.ExecutionEffect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.*;

import java.math.BigInteger;
import java.util.List;

/**
 * Abstract Executable Result of type Success containing the details of the
 * contract execution. It shows the result of a successful execution
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
@JsonTypeName("Success")
public class Success implements ExecutionResult {

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
     * List of Hex-encoded transfer address.
     */
    private List<String> transfers;
}
