package com.casper.sdk.model.transaction.entrypoint;

/**
 * The `activate bid` native entry point, used to reactivate an inactive bid.
 * <p>
 * Requires the following runtime args:
 * <ul><li>"validator_public_key": `PublicKey`</li></ul>
 *
 * @author ian@meywood.com
 */
public class ActivateBidEntryPoint extends TransactionEntryPoint {
    public ActivateBidEntryPoint() {
        super((byte) ACTIVATE_BID_TAG, "ActivateBid");
    }
}
