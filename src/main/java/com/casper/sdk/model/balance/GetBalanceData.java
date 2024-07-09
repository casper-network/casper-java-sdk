package com.casper.sdk.model.balance;

import com.casper.sdk.annotation.ExcludeFromJacocoGeneratedReport;
import com.casper.sdk.model.common.RpcResult;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

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
public class GetBalanceData extends RpcResult {

    /** The balance value */
    @JsonIgnore
    private BigInteger value;

    /** * The merkle proof */
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
