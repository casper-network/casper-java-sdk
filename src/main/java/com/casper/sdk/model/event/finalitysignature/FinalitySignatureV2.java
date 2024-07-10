package com.casper.sdk.model.event.finalitysignature;

import com.casper.sdk.model.common.Digest;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

/**
 * Validator's signature of a block, confirming it is finalized.
 *
 * @author ian@meywood.com
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@JsonTypeName("V2")
public class FinalitySignatureV2 extends FinalitySignature {
    /** The height of the associated block. */
    @JsonProperty("block_height")
    private BigInteger blockHeight;
    /** The hash of the chain name of the associated block. */
    @JsonProperty("chain_name_hash")
    private Digest chainNameHash;
}
