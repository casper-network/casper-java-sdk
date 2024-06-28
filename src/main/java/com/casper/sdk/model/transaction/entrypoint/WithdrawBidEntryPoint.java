package com.casper.sdk.model.transaction.entrypoint;

/**
 * The `withdraw_bid` native entry point, used to decrease a stake.
 * <p>
 * Requires the following runtime args:
 * <ul>
 * <li>public_key": `PublicKey`</li>
 * <li>"amount": `U512`</li>
 * </ul>
 *
 * @author ian@meywood.com
 */
public class WithdrawBidEntryPoint extends TransactionEntryPoint {

    public WithdrawBidEntryPoint() {
        super((byte) 4, "WithdrawBid");
    }
}
