package com.casper.sdk.model.deploy.executionresult;

import com.casper.sdk.model.deploy.ExecutionEffect;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
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
public class Success {

    /**
     * @see ExecutionEffect
     */
    private ExecutionEffect effect;

    /**
     * List of Hex-encoded transfer address.
     */
    private List<String> transfers;

    /**
     * The cost of executing the deploy.
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private BigInteger cost;
}
