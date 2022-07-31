package com.casper.sdk.model.balance;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.casper.sdk.annotation.ExcludeFromJacocoGeneratedReport;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;

/**
 * Root class for a Casper balance data request Result for "state_get_balance"
 * RPC response
 *
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
