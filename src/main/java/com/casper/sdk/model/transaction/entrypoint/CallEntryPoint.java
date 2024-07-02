package com.casper.sdk.model.transaction.entrypoint;

/**
 * The standard `call` entry point used in session code.
 *
 * @author ian@meywood.com
 */
public class CallEntryPoint extends TransactionEntryPoint {

    public CallEntryPoint() {
        super((byte)CALL_TAG, "Call");
    }
}
