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
public class CasperEra {

    @JsonProperty("era_report")
    private CasperEraReport eraReport;

    @JsonProperty("next_era_validator_weights")
    private List<CasperValidatorWeight> nextEraValidatorWeights;

}
