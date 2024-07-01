package com.casper.sdk.model.transaction.entrypoint;

/**
 * The `undelegate` native entry point, used to reduce a delegator's stake or remove the
 * delegator if the remaining stake is 0.
 * <p>
 * Requires the following runtime args:
 * <ul><li>delegator": `PublicKey`</li>
 * <li>"validator": `PublicKey`</li>
 * <li>"amount": `U512`</li></ul>
 *
 * @author ian@meywood.com
 */
public class UndelegateEntryPoint extends TransactionEntryPoint {
    public UndelegateEntryPoint() {
        super((byte) UNDELEGATE_TAG, "Undelegate");
    }
}
