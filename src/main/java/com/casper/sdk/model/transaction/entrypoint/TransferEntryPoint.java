package com.casper.sdk.model.transaction.entrypoint;

/**
 * The `transfer` native entry point, used to transfer `Motes` from a source purse to a target
 * purse.
 * <p>
 * Requires the following runtime args:
 * <ul><li>source": `URef`</li>
 * <li>"target": `URef`</li>
 * <li>"amount": `U512`</li></ul>
 * <p>
 * The following optional runtime args can also be provided:
 * <ul><li>to": `Option<AccountHash>`</li>
 * <li>"id": `Option<u64>`</li></ul>
 *
 * @author ian@meywood.com
 */
public class TransferEntryPoint extends TransactionEntryPoint {

    public TransferEntryPoint() {
        super((byte) 2, "Transfer");
    }
}
