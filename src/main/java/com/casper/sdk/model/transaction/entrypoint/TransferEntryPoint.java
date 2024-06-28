package com.casper.sdk.model.transaction.entrypoint;

/**
 * The `transfer` native entry point, used to transfer `Motes` from a source purse to a target
 * purse.
 * <p>
 * Requires the following runtime args:
 * * "source": `URef`
 * * "target": `URef`
 * * "amount": `U512`
 * <p>
 * The following optional runtime args can also be provided:
 * * "to": `Option<AccountHash>`
 * * "id": `Option<u64>`
 *
 * @author ian@meywood.com
 */
public class TransferEntryPoint extends TransactionEntryPoint {

    public TransferEntryPoint() {
        super((byte) 2, "Transfer");
    }
}
