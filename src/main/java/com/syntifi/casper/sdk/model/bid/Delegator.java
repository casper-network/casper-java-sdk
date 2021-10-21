package com.syntifi.casper.sdk.model.bid;

import java.math.BigInteger;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.syntifi.casper.sdk.annotation.ExcludeFromJacocoGeneratedReport;
import com.syntifi.casper.sdk.model.key.PublicKey;
import com.syntifi.casper.sdk.model.uref.URef;

import lombok.Data;

/**
 * Represents a party delegating their stake to a validator (or \"delegatee\")
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
@Data
public class Delegator {
    /**
     * @see PublicKey
     */
    @JsonProperty("validator_public_key")
    private PublicKey validatorPublicKey;

    /**
     * @see VestingSchedule
     */
    @JsonProperty("vesting_schedule")
    private VestingSchedule vestingSchedule;

    /**
     * @see URef
     */
    @JsonProperty("bonding_purse")
    private URef bondingPurse;
  
    /**
     * @see PublicKey
     */
    @JsonProperty("delegator_public_key")
    private PublicKey delegatorPublicKey;

    /**
     * ammount
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
