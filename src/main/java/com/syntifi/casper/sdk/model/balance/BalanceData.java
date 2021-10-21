package com.syntifi.casper.sdk.model.balance;

import java.math.BigInteger;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.syntifi.casper.sdk.annotation.ExcludeFromJacocoGeneratedReport;

import lombok.Data;

/**
 * Root class for a Casper balance data request Result for "state_get_balance"
 * RPC response
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
@Data
public class BalanceData {

    /**
     * The RPC API version
     */
    @JsonProperty("api_version")
    private String apiVersion;

    /**
     * The balance value
     */
    @JsonIgnore
    private BigInteger value;

    /**
     * The merkle proof
     */
    @JsonProperty("merkle_proof")
    private String merkleProof;

    @JsonProperty("balance_value")
    @ExcludeFromJacocoGeneratedReport
    protected String getJsonValue() {
        return this.value.toString(10);
    }

    @JsonProperty("balance_value")
    @ExcludeFromJacocoGeneratedReport
    protected void setJsonValue(String value) {
        this.value = new BigInteger(value, 10);
    }
}
