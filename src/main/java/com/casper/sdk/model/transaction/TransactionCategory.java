package com.casper.sdk.model.transaction;

import com.casper.sdk.exception.NoSuchTypeException;
import com.casper.sdk.model.clvalue.serde.CasperSerializableObject;
import com.casper.sdk.model.clvalue.serde.Target;
import com.casper.sdk.model.key.Tag;
import com.fasterxml.jackson.annotation.JsonValue;
import dev.oak3.sbs4j.SerializerBuffer;
import dev.oak3.sbs4j.exception.ValueSerializationException;

/**
 * The category of a Transaction.
 *
 * @author ian@meywood.com
 */
public enum TransactionCategory implements CasperSerializableObject, Tag {

    /** Standard transaction (the default). */
    STANDARD(0),
    /** Native mint interaction. */
    MINT(1),
    /** Native auction interaction. */
    AUCTION(2),
    /** Install or Upgrade. */
    INSTALL_UPGRADE(3);

    private final byte tag;

    TransactionCategory(final int tag) {
        this.tag = (byte) tag;
    }

    @Override
    @JsonValue
    public byte getByteTag() {
        return tag;
    }

    @Override
    public void serialize(SerializerBuffer ser, Target target) throws ValueSerializationException, NoSuchTypeException {
        ser.writeU8(getByteTag());
    }
}
