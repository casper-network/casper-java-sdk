package com.syntifi.casper.sdk.model.bid;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedHashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.syntifi.casper.sdk.annotation.ExcludeFromJacocoGeneratedReport;
import com.syntifi.casper.sdk.exception.InvalidByteStringException;
import com.syntifi.casper.sdk.model.key.PublicKey;
import com.syntifi.casper.sdk.model.uref.URef;

import lombok.Data;

/**
 * An entry in the validator map.
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
@Data
public class Bid {

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
     * This validator's delegators, indexed by their public keys
     */
    @JsonIgnore
    private Map<PublicKey, Delegator> delegators = new LinkedHashMap<>();

    /**
     * `true` if validator has been \"evicted\"
     */
    private boolean inactive;

    /**
     * The amount of tokens staked by a validator (not including delegators).
     */
    @JsonIgnore
    private BigInteger stakedAmount;

    /**
     * Validator PublicKey
     */
    @JsonProperty("validator_public_key")
    private PublicKey validatorPublicKey;

    /**
     * Vesting schedule for a genesis validator. `None` if non-genesis validator.
     */
    @JsonProperty("vesting_schedule")
    private VestingSchedule vestingSchedule;

    @JsonSetter("delegators")
    @ExcludeFromJacocoGeneratedReport
    protected void setJsonDelegators(Map<String, Delegator> node)
            throws NoSuchAlgorithmException, InvalidByteStringException {
        for (Map.Entry<String, Delegator> entry : node.entrySet()) {
            PublicKey publicKey = PublicKey.fromTaggedHexString(entry.getKey());
            Delegator delegator = entry.getValue();
            this.delegators.put(publicKey, delegator);
        }
    }

    @JsonGetter("delegators")
    @ExcludeFromJacocoGeneratedReport
    protected Map<String, Delegator> getJsonDelegators() {
        Map<String, Delegator> out = new LinkedHashMap<>();
        for (Map.Entry<PublicKey, Delegator> entry : this.delegators.entrySet()) {
            out.put(entry.getKey().getAlgoTaggedHex(), entry.getValue());
        }
        return out;
    }

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
