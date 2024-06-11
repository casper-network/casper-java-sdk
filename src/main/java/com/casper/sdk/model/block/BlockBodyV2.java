package com.casper.sdk.model.block;

import com.casper.sdk.model.common.Digest;
import com.casper.sdk.model.transaction.TransactionCategory;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;
import java.util.Map;

/**
 * V2 of the block body
 *
 * @author ian@meywood.com
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BlockBodyV2 extends BlockBody {
    /** The body's hash. */
    @JsonProperty("hash")
    private Digest hash;

    /** List of Hex-encoded hash digest */
    @JsonProperty("deploy_hashes")
    private List<Digest> deployHashes;

    /** List of Hex-encoded hash digest */
    @JsonProperty("transfer_hashes")
    private List<Digest> transferHashes;

    @JsonProperty("rewarded_signatures")
    private List<List<Long>> rewardedSignatures;

    @JsonProperty("transactions")
    private Map<TransactionCategory, List<Digest>> transactions;
}
