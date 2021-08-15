package com.syntifi.casper.sdk.model.storedvalue;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * Root class for a Casper Stored Value
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
@Data
public class CasperStoredValueData {

    @JsonProperty("api_version")
    private String apiVersion;

    @JsonProperty("stored_value")
    private CasperStoredValue<? extends Object> casperStoredValue;

    @JsonProperty("merkle_proof")
    private String merkleProof;
}
