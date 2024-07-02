package com.casper.sdk.model.transaction.entrypoint;

/**
 * The `change_bid_public_key` native entry point, used to change a bid's public key.
 * <p>
 * Requires the following runtime args:
 * <ul><li>"public_key": `PublicKey`</li>
 * <li>"new_public_key": `PublicKey`</li></ul>
 *
 * @author ian@meywood.com
 */
public class ChangeBidPublicKeyEntryPoint extends TransactionEntryPoint {
    public ChangeBidPublicKeyEntryPoint() {
        super((byte) CHANGE_BID_PUBLIC_KEY_TAG,"ChangeBidPublicKey");
    }
}
