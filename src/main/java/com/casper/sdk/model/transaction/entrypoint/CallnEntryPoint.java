package com.casper.sdk.model.transaction.entrypoint;

/**
 * The standard `call` entry point used in session code.
 *
 * @author ian@meywood.com
 */
public class CallnEntryPoint extends TransactionEntryPoint {

    public CallnEntryPoint() {
        super((byte)0, "Call");
    }
}
