package com.casper.sdk.model.bid;

import com.casper.sdk.model.key.PublicKey;
import com.casper.sdk.model.uref.URef;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;

/**
 * Validator bid record.
 *
 * @author ian@meywood.com
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ValidatorBid implements BidKind {
    /** Validator public key */
    @JsonProperty("validator_public_key")
    private PublicKey validatorPublicKey;
    /** The purse that was used for bonding. */
    @JsonProperty("bonding_purse")
    private URef bondingPurse;
    /** The amount of tokens staked by a validator (not including delegators). */
    @JsonProperty("staked_amount")
    private BigInteger stakedAmount;
    /** Delegation rate */
    @JsonProperty("delegation_rate")
    private byte delegationRate;
    /** Vesting schedule for a genesis validator. `None` if non-genesis validator. */
    @JsonProperty("vesting_schedule")
    private VestingSchedule vestingSchedule;
    /** `true` if validator has been "evicted" */
    @JsonProperty("inactive")
    private boolean inactive;
}
