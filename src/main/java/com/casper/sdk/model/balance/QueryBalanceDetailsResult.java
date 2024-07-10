package com.casper.sdk.model.balance;

import com.casper.sdk.model.common.RpcResult;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.util.List;

/**
 * Result for "query_balance_details" RPC response.
 *
 * @author ian@meywood.com
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class QueryBalanceDetailsResult extends RpcResult {
    /** The purses total balance, not considering holds. */
    @JsonProperty("total_balance")
    private BigInteger totalBalance;
    /** The available balance in motes (total balance - sum of all active holds). */
    @JsonProperty("available_balance")
    private BigInteger availableBalance;
    /** A proof that the given value is present in the Merkle trie. */
    @JsonProperty("total_balance_proof")
    private String totalBalanceProof;
    /** Holds active at the requested point in time. */
    @JsonProperty("holds")
    private List<BalanceHoldWithProof> holds;
}
