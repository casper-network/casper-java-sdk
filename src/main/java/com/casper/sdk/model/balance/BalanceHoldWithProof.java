package com.casper.sdk.model.balance;

import lombok.*;

import java.math.BigInteger;

/**
 * Holds active at the requested point in time.
 *
 * @author ian@meywood.com
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class BalanceHoldWithProof {
    /** The block time at which the hold was created. */
    private long time;
    /** The amount in the hold. */
    private BigInteger amount;
    /** A proof that the given value is present in the Merkle trie. */
    private String proof;
}
