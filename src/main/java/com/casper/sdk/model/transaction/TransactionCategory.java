package com.casper.sdk.model.transaction;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * The category of a Transaction.
 *
 * @author ian@meywood.com
 */
public enum TransactionCategory {

    /** Standard transaction (the default). */
    STANDARD(0),
    /** Native mint interaction. */
    MINT(1),
    /** Native auction interaction. */
    AUCTION(2),
    /** Install or Upgrade. */
    INSTALL_UPGRADE(3);

    private final byte value;

    TransactionCategory(final int value) {
        this.value = (byte) value;
    }

    @JsonValue
    public byte toValue() {
        return value;
    }
}
