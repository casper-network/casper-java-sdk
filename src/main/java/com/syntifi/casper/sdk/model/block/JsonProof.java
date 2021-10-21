package com.syntifi.casper.sdk.model.block;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.syntifi.casper.sdk.model.key.PublicKey;
import com.syntifi.casper.sdk.model.key.Signature;

import lombok.Data;

/**
 * Holds the block proof data
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @see JsonBlock
 * @since 0.0.1
 */
@Data
public class JsonProof {

    /**
     * @see PublicKey
     */
    @JsonProperty("public_key")
    private PublicKey publicKey;

    /**
     * @see Signature
     */
    private Signature signature;
}
