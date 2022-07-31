package com.casper.sdk.model.era;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;
import java.util.List;

/**
 * An entry in the validator map.
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
