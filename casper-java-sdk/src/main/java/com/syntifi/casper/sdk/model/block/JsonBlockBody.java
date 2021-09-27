package com.syntifi.casper.sdk.model.block;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.syntifi.casper.sdk.model.key.PublicKey;

import lombok.Data;

/**
 * A JSON-friendly representation of `Body`
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @see JsonBlock
 * @since 0.0.1
 */
@Data
public class JsonBlockBody {

    /**
     * @see PublicKey 
     */
    @JsonProperty("proposer")
    private PublicKey proposer;

    /**
     * List of Hex-encoded hash digest
     */
    @JsonProperty("deploy_hashes")
    private List<String> deployHashes;

    /**
     * List of Hex-encoded hash digest
     */
    @JsonProperty("transfer_hashes")
    private List<String> transferHashes;

}
