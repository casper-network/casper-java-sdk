package com.casper.sdk.model.transaction.entrypoint;

/**
 * The `add_bid` native entry point, used to create or top off a bid purse.
 * <p>
 * Requires the following runtime args:
 * <ul>
 * <li>"public_key": `PublicKey`</li>
 * <li>"delegation_rate": `u8`</li>
 * <li>"amount": `U512`</li>
 * <li>"minimum_delegation_amount": `u64`</li>
 * </ul>
 *
 * @author ian@meywood.com
 */
public class AddBidEntryPoint extends TransactionEntryPoint {

    public AddBidEntryPoint() {
        super((byte) ADD_BID_TAG, "AddBid");
    }
}
