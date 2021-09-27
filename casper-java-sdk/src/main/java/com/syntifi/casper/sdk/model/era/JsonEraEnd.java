package com.syntifi.casper.sdk.model.era;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

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
     * List of @see JsonValidatorWeight
     */
    @JsonProperty("next_era_validator_weights")
    private List<JsonValidatorWeight> nextEraValidatorWeights;

}
