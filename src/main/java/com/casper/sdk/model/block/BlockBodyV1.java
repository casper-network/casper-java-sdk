package com.casper.sdk.model.block;

import com.casper.sdk.model.common.Digest;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

/**
 * A JSON-friendly representation of `Body`
 *
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @see BlockV1
 * @since 0.0.1
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BlockBodyV1 extends BlockBody {

    /** List of Hex-encoded hash digest */
    @JsonProperty("deploy_hashes")
    private List<Digest> deployHashes;

    /** List of Hex-encoded hash digest */
    @JsonProperty("transfer_hashes")
    private List<Digest> transferHashes;
}
