package com.casper.sdk.model.deploy;

import com.casper.sdk.model.key.PublicKey;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The SeigniorageAllocation for DelegatorKind
 *
 * @author ian@meywood.com
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonTypeName("DelegatorKind")
public class DelegatorKindAllocation extends SeigniorageAllocation {

    @JsonProperty("delegator_kind")
    private DelegatorKind delegatorKind;

    @JsonProperty("validator_public_key")
    private PublicKey validatorPublicKey;
}
