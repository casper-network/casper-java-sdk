package com.casper.sdk.model.block;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * A JSON-friendly representation of `Block`
 *
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @see JsonBlockData
 * @since 0.0.1
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JsonBlock {

    /**
     * The block's hash.
     */
    @JsonProperty("hash")
    private String hash;

    /**
     * {@link JsonBlockHeader}
     */
    @JsonProperty("header")
    private JsonBlockHeader header;

    /**
     * {@link JsonBlockBody}
     */
    @JsonProperty("body")
    private JsonBlockBody body;

    /**
     * List of {@link JsonProof}
     */
    @JsonProperty("proofs")
    private List<JsonProof> proofs;
}
