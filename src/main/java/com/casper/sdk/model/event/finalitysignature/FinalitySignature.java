package com.casper.sdk.model.event.finalitysignature;

import com.casper.sdk.model.common.Digest;
import com.casper.sdk.model.event.EventData;
import com.casper.sdk.model.key.PublicKey;
import com.casper.sdk.model.key.Signature;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Interface for V1 and V2 finality signatures.
 *
 * @author ian@meywood.com
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
@JsonSubTypes({
        @JsonSubTypes.Type(value = FinalitySignatureV1.class, name = "V1"),
        @JsonSubTypes.Type(value = FinalitySignatureV2.class, name = "V2")})
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public abstract class FinalitySignature implements EventData {
    /** The block hash of the associated block. */
    @JsonProperty("block_hash")
    private Digest blockHash;
    /** The era in which the associated block was created. */
    @JsonProperty("era_id")
    private long eraId;
    /** The public key of the signing validator. */
    @JsonProperty("public_key")
    private PublicKey publicKey;
    /** The signature over the block hash of the associated block. */
    @JsonProperty("signature")
    private Signature signature;
}
