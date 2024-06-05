package com.casper.sdk.model.block;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

/**
 * A JSON-friendly representation of `Block`
 *
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @see JsonBlockData
 * @since 0.0.1
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BlockV1 extends Block<JsonBlockHeader, BlockBodyV1> {

    /**
     * {@link JsonBlockHeader}
     */
    @JsonProperty("header")
    private JsonBlockHeader header;

    /**
     * {@link BlockBodyV1}
     */
    @JsonProperty("body")
    private BlockBodyV1 body;

    /**
     * List of {@link JsonProof}
     */
    @JsonProperty("proofs")
    private List<JsonProof> proofs;
}
