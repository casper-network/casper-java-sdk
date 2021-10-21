package com.syntifi.casper.sdk.model.deploy;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.syntifi.casper.sdk.model.key.PublicKey;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@JsonTypeName("Delegator")
public class Delegator extends SeigniorageAllocation {

    /**
     * Delegator's public key
     * 
     * @see PublicKey
     */
    @JsonProperty("delegator_public_key")
    private PublicKey delegatorPublicKey;

    /**
     * Validator's public key
     * 
     * @see PublicKey
     */
    @JsonProperty("validator_public_key")
    private PublicKey validatorPublicKey;
}