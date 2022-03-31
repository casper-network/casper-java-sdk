package com.syntifi.casper.sdk.model.deploy.executionresult;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.syntifi.casper.sdk.annotation.ExcludeFromJacocoGeneratedReport;
import com.syntifi.casper.sdk.model.deploy.ExecutionEffect;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;
import java.util.List;

/**
 * Abstract Executable Result of type Success containing the details of the
 * contract execution. It shows the result of a successs execution
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

    /**
     * getter for cost json serialization
     *
     * @return cost as expected for json serialization
     */
    @JsonProperty("cost")
    @ExcludeFromJacocoGeneratedReport
    protected String getJsonCost() {
        return this.cost.toString(10);
    }

    /**
     * setter for cost from json deserialized value
     *
     * @param value the deserialized value
     */
    @JsonProperty("cost")
    @ExcludeFromJacocoGeneratedReport
    protected void setJsonCost(String value) {
        this.cost = new BigInteger(value, 10);
    }
}
