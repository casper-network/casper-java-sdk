package com.casper.sdk.model.transaction.entrypoint;

/**
 * The `change_bid_public_key` native entry point, used to change a bid's public key.
 * <p>
 * Requires the following runtime args:
 * * "public_key": `PublicKey`
 * * "new_public_key": `PublicKey`
 *
 * @author ian@meywood.com
 */
public class ChangeBidPublicKeyEntryPoint extends TransactionEntryPoint {
    public ChangeBidPublicKeyEntryPoint() {
        super((byte) 9,"ChangeBidPublicKey");
    }
}
