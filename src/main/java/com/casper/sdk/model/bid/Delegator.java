package com.casper.sdk.model.bid;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.casper.sdk.annotation.ExcludeFromJacocoGeneratedReport;
import com.casper.sdk.model.key.PublicKey;
import com.casper.sdk.model.uref.URef;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;

/**
 * Represents a party delegating their stake to a validator (or \"delegatee\")
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
     * staked amount
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
