package com.casper.sdk.model.transaction.entrypoint;

/**
 * The `add_bid` native entry point, used to create or top off a bid purse.
 * <p>
 * Requires the following runtime args:
 * * "public_key": `PublicKey`
 * * "delegation_rate": `u8`
 * * "amount": `U512`
 * * "minimum_delegation_amount": `u64`
 *
 * @author ian@meywood.com
 */
public class AddBidEntryPoint extends TransactionEntryPoint {

    public AddBidEntryPoint() {
        super((byte) 3, "AddBid");
    }
}
