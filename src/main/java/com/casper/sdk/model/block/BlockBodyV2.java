package com.casper.sdk.model.block;

import com.casper.sdk.model.common.Digest;
import com.casper.sdk.model.transaction.TransactionCategory;
import com.casper.sdk.model.transaction.TransactionHash;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    @JsonProperty("transfer_hashes")
    private List<Digest> transferHashes;

    @JsonProperty("rewarded_signatures")
    private List<List<Long>> rewardedSignatures;

    @JsonProperty("transactions")
    private Map<TransactionCategory, List<TransactionHash>> transactions;

    public List<List<Long>> getRewardedSignatures() {
        return rewardedSignatures != null ? rewardedSignatures : Collections.emptyList();
    }

    public List<Digest> getTransferHashes() {
        return transferHashes != null ? transferHashes : Collections.emptyList();
    }

    @JsonIgnore
    public List<Digest> getFlatTransactions() {
        return transactions.values()
                .stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }
}
