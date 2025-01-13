package com.casper.sdk.model.deploy;

import com.casper.sdk.model.key.PublicKey;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonTypeName("Delegator")
public class Delegator extends SeigniorageAllocation {

    /**
     * Delegator's kind
     */
    @JsonProperty("delegator_kind")
    private DelegatorKind delegatorKind;

    /**
     * Validator's public key
     *
     * @see PublicKey
     */
    @JsonProperty("validator_public_key")
    private PublicKey validatorPublicKey;
}