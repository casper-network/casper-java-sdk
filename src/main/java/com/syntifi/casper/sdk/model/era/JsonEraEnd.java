package com.syntifi.casper.sdk.model.era;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Casper block root Era data
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
public class JsonEraEnd {
    
    /**
     * @see JsonEraReport
     */
    @JsonProperty("era_report")
    private JsonEraReport eraReport;

    /**
     * @see JsonValidatorWeight
     */
    @JsonProperty("next_era_validator_weights")
    private List<ValidatorWeight> nextEraValidatorWeights;
}
