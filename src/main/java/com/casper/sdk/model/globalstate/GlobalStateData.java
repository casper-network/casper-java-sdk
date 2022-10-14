package com.casper.sdk.model.globalstate;

import com.casper.sdk.model.block.JsonBlockHeader;
import com.casper.sdk.model.storedvalue.StoredValue;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/**
 * Holds the header data of a Global State
 *
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.2.0
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GlobalStateData {

    /**
     * The RPC API version
     */
    @JsonProperty("api_version")
    private String apiVersion;

    /**
     * {@link JsonBlockHeader}
     */
    @JsonProperty("block_header")
    private JsonBlockHeader header;

    /**
     * Merkle proof
     */
    @JsonProperty("merkle_proof")
    private String merkleProof;

    @JsonProperty("stored_value")
    private StoredValue<?> storedValue;
}
