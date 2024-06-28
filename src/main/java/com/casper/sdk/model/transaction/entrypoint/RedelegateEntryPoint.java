package com.casper.sdk.model.transaction.entrypoint;

/**
 * The `redelegate` native entry point, used to reduce a delegator's stake or remove the
 * delegator if the remaining stake is 0, and after the unbonding delay, automatically
 * delegate to a new validator.
 * <p>
 * Requires the following runtime args:
 * <ul><li>delegator": `PublicKey`</li>
 * <li>"validator": `PublicKey`</li>
 * <li>"amount": `U512`</li>
 * <li>"new_validator": `PublicKey`</li></ul>
 *
 * @author ian@meywood.com
 */
public class RedelegateEntryPoint extends TransactionEntryPoint {
    public RedelegateEntryPoint() {
        super((byte) 7, "Redelegate");
    }
}
