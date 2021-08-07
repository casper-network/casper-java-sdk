package com.syntifi.casper.sdk.model.era;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.1.0
 */
public class CasperEra {
    @JsonProperty("era_report")
    private CasperEraReport eraReport;

    @JsonProperty("next_era_validator_weights")
    private List<CasperValidatorWeight> nextEraValidatorWeights;

}
