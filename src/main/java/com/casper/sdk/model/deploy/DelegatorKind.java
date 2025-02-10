package com.casper.sdk.model.deploy;

import com.casper.sdk.model.key.PublicKey;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;

/**
 * Auction bid variants. Kinds of delegation bids
 *
 * @author carl@stormeye.co.uk
 */
@Getter
@Setter
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
@JsonSubTypes({
        @JsonSubTypes.Type(value = DelegatorKindPurse.class, name = "Purse"),
        @JsonSubTypes.Type(value = DelegatorKindPublicKey.class, name = "PublicKey")})
public class DelegatorKind extends SeigniorageAllocation {

    /** Delegator's kind */
    @JsonProperty("delegator_kind")
    private DelegatorKind delegatorKind;

    /** Validator PublicKey */
    @JsonProperty("validator_public_key")
    private PublicKey validatorPublicKey;

    @JsonProperty("amount")
    private BigInteger amount;

}
