package com.syntifi.casper.sdk.model.era;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.syntifi.casper.sdk.model.storedvalue.StoredValueEraInfo;

import lombok.Data;

/**
 * The summary of an era
 * 
 * 
 */
@Data
public class EraSummary {
    
    /**
     * The block hash
     */
    @JsonProperty("block_hash")
    private String blockHash;

    /**
     * The era id
     */
    @JsonProperty("era_id")
    private Long eraId;

    /**
     * The merkle proof
     */
    @JsonProperty("merkle_proof")
    private String merkleProof;

    /**
     * Hex-encoded hash of the state root
     */
    @JsonProperty("state_root_hash")
    private String stateRootHash;

    /**
     * The StoredValue containing era information
     */
    @JsonProperty("stored_value")
    private StoredValueEraInfo storedValue;
}
