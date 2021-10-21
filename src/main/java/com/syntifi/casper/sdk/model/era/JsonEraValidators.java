package com.syntifi.casper.sdk.model.era;

import java.math.BigInteger;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * An entry in the validator map.
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
@Data
public class JsonEraValidators {

    /**
     * Era id
     */
    @JsonProperty("era_id")
    private BigInteger eraId;

    /**
     * @see JsonValidatorWeight
     */
    @JsonProperty("validator_weights")
    private List<JsonValidatorWeight> validatorWeights;
}
