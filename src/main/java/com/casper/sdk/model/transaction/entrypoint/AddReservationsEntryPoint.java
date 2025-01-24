package com.casper.sdk.model.transaction.entrypoint;

/**
 * The `add_reservations` native entry point, used to add delegators to validator's reserve
 * list.
 *
 * @author ian@meywood.com
 */
public class AddReservationsEntryPoint extends TransactionEntryPoint {
    public AddReservationsEntryPoint() {
        super((byte) ADD_RESERVATIONS_TAG, "AddReservations");
    }
}
