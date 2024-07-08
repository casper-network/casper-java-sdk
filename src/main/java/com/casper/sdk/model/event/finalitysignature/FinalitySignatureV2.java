package com.casper.sdk.model.event.finalitysignature;

import com.casper.sdk.model.common.Digest;
import com.casper.sdk.model.key.PublicKey;
import com.casper.sdk.model.key.Signature;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

/**
 *  Validator's signature of a block, confirming it is finalized.
 *
 * @author ian@meywood.com
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@JsonTypeName("V2")
public class FinalitySignatureV2 implements FinalitySignature {
    /**  The block hash of the associated block. */
    @JsonProperty("block_hash")
    private Digest blockHash;
    /** The height of the associated block. */
    @JsonProperty("block_height")
    private BigInteger blockHeight;
    /** The era in which the associated block was created. */
    @JsonProperty("era_id")
    private long eraId;
    /** The hash of the chain name of the associated block. */
    @JsonProperty("chain_name_hash")
    private Digest chainNameHash;
    /** The signature over the block hash of the associated block. */
    @JsonProperty("signature")
    private Signature signature;
    /** The public key of the signing validator. */
    @JsonProperty("public_key")
    private PublicKey publicKey;
}
