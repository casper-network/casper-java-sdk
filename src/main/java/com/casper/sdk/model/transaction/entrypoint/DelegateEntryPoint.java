package com.casper.sdk.model.transaction.entrypoint;

/**
 * The `delegate` native entry point, used to add a new delegator or increase an existing
 * delegator's stake.
 * <p>
 * Requires the following runtime args:
 * <ul><li>"delegator": `PublicKey`</li>
 * <li>"validator": `PublicKey`</li>
 * <li>"amount": `U512`</li></ul>
 *
 * @author ian@meywood.com
 */
public class DelegateEntryPoint extends TransactionEntryPoint {
    public DelegateEntryPoint() {
        super((byte) 5, "Delegate");
    }
}
