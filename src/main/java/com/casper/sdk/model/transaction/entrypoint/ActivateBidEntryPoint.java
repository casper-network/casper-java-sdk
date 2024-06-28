package com.casper.sdk.model.transaction.entrypoint;

/**
 * The `activate bid` native entry point, used to reactivate an inactive bid.
 * <p>
 * Requires the following runtime args:
 * * "validator_public_key": `PublicKey`
 *
 * @author ian@meywood.com
 */
public class ActivateBidEntryPoint extends TransactionEntryPoint {
    public ActivateBidEntryPoint() {
        super((byte) 8, "ActivateBid");
    }
}
