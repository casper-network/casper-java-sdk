package com.syntifi.casper.sdk.model.deploy;

import java.math.BigInteger;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.syntifi.casper.sdk.annotation.ExcludeFromJacocoGeneratedReport;

import lombok.Data;

/**
 * Info about a seigniorage allocation for a validator
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
@Data
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
@JsonSubTypes({ @JsonSubTypes.Type(value = Validator.class, name = "Validator"),
        @JsonSubTypes.Type(value = Delegator.class, name = "Delegator") })
public class SeigniorageAllocation {

    /**
     * Allocated amount
     */
    @JsonIgnore
    private BigInteger amount;

    @JsonProperty("amount")
    @ExcludeFromJacocoGeneratedReport
	protected String getJsonAmount() {
        return this.amount.toString(10);
    }

    @JsonProperty("amount")
    @ExcludeFromJacocoGeneratedReport
	protected void setJsonAmount(String value) {
        this.amount = new BigInteger(value, 10);
    }
}
