package com.syntifi.casper.sdk.model.era;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.1.0
 */
@Data
public class CasperEraReport {
    @JsonProperty("inactive_validators")
    private List<String> inactiveValidators;

    private List<String> equivocators;
    
    private List<CasperReward> rewards;
}
