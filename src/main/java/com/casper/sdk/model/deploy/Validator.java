package com.casper.sdk.model.deploy;

import com.casper.sdk.model.key.PublicKey;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonTypeName("Validator")
public class Validator extends SeigniorageAllocation {

    /**
     * Validator's public key
     *
     * @see PublicKey
     */
    @JsonProperty("validator_public_key")
    private PublicKey validatorPublicKey;
}