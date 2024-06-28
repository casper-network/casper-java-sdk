package com.casper.sdk.model.transaction.entrypoint;

/**
 * The `undelegate` native entry point, used to reduce a delegator's stake or remove the
 * delegator if the remaining stake is 0.
 * <p>
 * Requires the following runtime args:
 * * "delegator": `PublicKey`
 * * "validator": `PublicKey`
 * * "amount": `U512`
 *
 * @author ian@meywood.com
 */
public class UndelegateEntryPoint extends TransactionEntryPoint {
    public UndelegateEntryPoint() {
        super((byte) 6, "Undelegate");
    }
}
