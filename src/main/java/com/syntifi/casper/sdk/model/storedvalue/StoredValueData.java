package com.syntifi.casper.sdk.model.storedvalue;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Root class for a Casper Stored Value
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class StoredValueData {
    @JsonProperty("api_version")
    private String apiVersion;

    @JsonProperty("stored_value")
    private StoredValue<?> storedValue;

    @JsonProperty("merkle_proof")
    private String merkleProof;
}
