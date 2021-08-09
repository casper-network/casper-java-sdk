package com.syntifi.casper.sdk.model.block;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * Holds the block proof data
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @see CasperBlock
 * @since 0.0.1
 */
@Data
public class CasperBlockProof {
    @JsonProperty("public_key")
    private String publicKey;

    private String signature;
}
