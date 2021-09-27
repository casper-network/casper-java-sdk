package com.syntifi.casper.sdk.model.block;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * A JSON-friendly representation of `Block`
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @see JsonBlockData
 * @since 0.0.1
 */
@Data
public class JsonBlock {

    /**
     * The block's hash.
     */
    @JsonProperty("hash")
    private String hash;

    /**
     * @see JsonBlockHeader
     */
    @JsonProperty("header")
    private JsonBlockHeader header;

    /**
     * @see JsonBlockBody
     */
    @JsonProperty("body")
    private JsonBlockBody body;

    /**
     * List of @see JsonProof
     */
    @JsonProperty("proofs")
    private List<JsonProof> proofs;
}
