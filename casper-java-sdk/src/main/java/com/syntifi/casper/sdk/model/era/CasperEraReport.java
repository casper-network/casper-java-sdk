package com.syntifi.casper.sdk.model.era;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * Casper block era report data
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @see CasperEra
 * @since 0.0.1
 */
@Data
public class CasperEraReport {
    @JsonProperty("inactive_validators")
    private List<String> inactiveValidators;

    private List<String> equivocators;
    
    private List<CasperReward> rewards;
}
