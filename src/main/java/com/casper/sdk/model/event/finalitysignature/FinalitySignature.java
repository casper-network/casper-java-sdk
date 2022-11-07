package com.casper.sdk.model.event.finalitysignature;

import com.casper.sdk.model.common.Digest;
import com.casper.sdk.model.event.EventData;
import com.casper.sdk.model.key.PublicKey;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.*;

/**
 * A validator's signature of a block, to confirm it is finalized. Clients and joining nodes should wait until the
 * signers' combined weight exceeds their fault tolerance threshold before accepting the block as finalized.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonTypeName("FinalitySignature")
public class FinalitySignature implements EventData {

    @JsonProperty("block_hash")
    private Digest blockHash;

    @JsonProperty("era_id")
    private long eraId;

    @JsonProperty("public_key")
    private PublicKey publicKey;

    @JsonProperty("signature")
    private String signature;
}
