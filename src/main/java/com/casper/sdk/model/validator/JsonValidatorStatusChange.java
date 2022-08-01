package com.casper.sdk.model.validator;

import com.casper.sdk.model.deploy.EraInfo;
import com.casper.sdk.model.key.PublicKey;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigInteger;
import java.time.chrono.Era;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * A single change to a validator's status in the given era
 *
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.2.0
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JsonValidatorStatusChange {

    /**
     * The era in which the change occurred
     */
    @JsonProperty("era_id")
    private BigInteger eraId;

    /**
     * The change in validator status
     *
     * @see JsonValidatorStatusChange
     */
    @JsonProperty("validator_change")
    private ValidatorChange validatorChange;

}
