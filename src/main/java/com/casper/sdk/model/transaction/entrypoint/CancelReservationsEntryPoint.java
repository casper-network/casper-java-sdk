package com.casper.sdk.model.transaction.entrypoint;

/**
 * @author ian@meywood.com
 */
public class CancelReservationsEntryPoint extends TransactionEntryPoint {
    public CancelReservationsEntryPoint() {
        super((byte) CANCEL_RESERVATIONS_TAG, "CancelReservations");
    }
}

