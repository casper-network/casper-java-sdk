package com.casper.sdk.model.deploy.executionresult;

import com.casper.sdk.annotation.ExcludeFromJacocoGeneratedReport;
import com.casper.sdk.model.deploy.ExecutionEffect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.math.BigInteger;

/**
 * Abstract Executable Result containing the details of the contract execution.
 * It can be any of the following types:
 *
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @see Failure
 * @see Success
 * @since 0.0.1
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Failure.class, name = "Failure"),
        @JsonSubTypes.Type(value = Success.class, name = "Success")
})
public interface ExecutionResult {
    BigInteger getCost();

    void setCost(BigInteger cost);

    ExecutionEffect getEffect();

    void setEffect(ExecutionEffect effect);

    /**
     * getter for cost json serialization
     *
     * @return cost as expected for json serialization
     */
    @JsonProperty("cost")
    @ExcludeFromJacocoGeneratedReport
    default String getJsonCost() {
        return this.getCost().toString(10);
    }

    /**
     * setter for cost from json deserialized value
     *
     * @param value the deserialized value
     */
    @JsonProperty("cost")
    @ExcludeFromJacocoGeneratedReport
    default void setJsonCost(String value) {
        this.setCost(new BigInteger(value, 10));
    }
}
