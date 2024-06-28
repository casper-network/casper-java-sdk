package com.casper.sdk.model.transaction.entrypoint;

/**
 * The `withdraw_bid` native entry point, used to decrease a stake.
 * <p>
 * Requires the following runtime args:
 * * "public_key": `PublicKey`
 * * "amount": `U512`
 *
 * @author ian@meywood.com
 */
public class WithdrawBidEntryPoint extends TransactionEntryPoint {


    public WithdrawBidEntryPoint() {
        super((byte) 4, "WithdrawBid");
    }
}
