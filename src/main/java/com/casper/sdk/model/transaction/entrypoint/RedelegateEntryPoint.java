package com.casper.sdk.model.transaction.entrypoint;

/**
 * The `redelegate` native entry point, used to reduce a delegator's stake or remove the
 * delegator if the remaining stake is 0, and after the unbonding delay, automatically
 * delegate to a new validator.
 * <p>
 * Requires the following runtime args:
 * * "delegator": `PublicKey`
 * * "validator": `PublicKey`
 * * "amount": `U512`
 * * "new_validator": `PublicKey`
 *
 * @author ian@meywood.com
 */
public class RedelegateEntryPoint extends TransactionEntryPoint {
    public RedelegateEntryPoint() {
        super((byte) 7, "Redelegate");
    }
}
