package com.casper.sdk.model.block;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

/**
 * Block and its associated signatures.
 *
 * @author ian@meywood.com
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BlockWithSignatures {

    @JsonProperty("block")
    private Block<?, ?> block;

    @JsonProperty("proofs")
    private List<JsonProof> proofs;

    public <BlockT extends Block<?, ?>> BlockT getBlock() {
        //noinspection unchecked
        return (BlockT) block;
    }
}
