package com.casper.sdk.model.block;

import com.casper.sdk.model.key.PublicKey;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.casper.sdk.model.key.Signature;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Holds the block proof data
 *
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @see JsonBlock
 * @since 0.0.1
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
