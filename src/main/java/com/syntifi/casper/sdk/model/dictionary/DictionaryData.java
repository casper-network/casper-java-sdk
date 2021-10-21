package com.syntifi.casper.sdk.model.dictionary;
    

import com.fasterxml.jackson.annotation.JsonProperty;
import com.syntifi.casper.sdk.model.storedvalue.StoredValue;

import lombok.Data;

/**
 * Dictionary key and stored value
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
@Data
public class DictionaryData {
    
    /**
     * The RPC API version 
     */
    @JsonProperty("api_version")
    private String apiVersion;

    /**
     * The key under which the value is stored
     */
    @JsonProperty("dictionary_key")
    private String dictionaryKey;

    /**
     * The merkle proof 
     */
    @JsonProperty("merkle_proof")
    private String merkleProof;

    /**
     * The stored value 
     */
    @JsonProperty("stored_value")
    private StoredValue<?> storedValue;
}