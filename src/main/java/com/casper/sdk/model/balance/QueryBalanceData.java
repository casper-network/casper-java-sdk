package com.casper.sdk.model.balance;

import com.casper.sdk.annotation.ExcludeFromJacocoGeneratedReport;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;

/**
 * Root class for a Casper balance data request Result for "query_balance"
 * RPC response
 *
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.2.0
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QueryBalanceData {

    /**
     * The RPC API version
     */
    @JsonProperty("api_version")
    private String apiVersion;

    /**
     * The balance value
     */
    @JsonIgnore
    private BigInteger balance;

    @JsonProperty("balance")
    @ExcludeFromJacocoGeneratedReport
    protected String getJsonValue() {
        return this.balance.toString(10);
    }

    @JsonProperty("balance")
    @ExcludeFromJacocoGeneratedReport
    protected void setJsonValue(String value) {
        this.balance = new BigInteger(value, 10);
    }
   
}
