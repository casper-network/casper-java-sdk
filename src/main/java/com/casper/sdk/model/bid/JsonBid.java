package com.casper.sdk.model.bid;

import java.math.BigInteger;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.casper.sdk.annotation.ExcludeFromJacocoGeneratedReport;
import com.casper.sdk.model.uref.URef;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * An entry in a founding validator map representing a bid.
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
public class JsonBid {

    /**
     * The purse that was used for bonding.
     */
    @JsonProperty("bonding_purse")
    private URef bondingPurse;

    /**
     * Delegation rate
     */
    @JsonProperty("delegation_rate")
    private int delegationRate;

    /**
     * The delegators
     * 
     * @see JsonDelegator
     */
    private List<JsonDelegator> delegators;

    /**
     * Is this an inactive validator?
     */
    private boolean inactive;

    /**
     * The amount of tokens staked by a validator (not including delegators).
     */
    @JsonIgnore
    private BigInteger stakedAmount;

    @JsonProperty("staked_amount")
    @ExcludeFromJacocoGeneratedReport
    protected String getJsonStakedAmount() {
        return this.stakedAmount.toString(10);
    }

    @JsonProperty("staked_amount")
    @ExcludeFromJacocoGeneratedReport
    protected void setJsonStakedAmount(String value) {
        this.stakedAmount = new BigInteger(value, 10);
    }
}
