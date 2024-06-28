package com.casper.sdk.model.transaction.target;

import com.casper.sdk.exception.NoSuchTypeException;
import com.casper.sdk.model.clvalue.serde.CasperSerializableObject;
import com.casper.sdk.model.clvalue.serde.Target;
import com.casper.sdk.model.key.Tag;
import com.fasterxml.jackson.annotation.JsonProperty;
import dev.oak3.sbs4j.SerializerBuffer;
import dev.oak3.sbs4j.exception.ValueSerializationException;

/**
 * The runtime used to execute a `Transaction`.
 *
 * @author ian@meywood.com
 */
public enum TransactionRuntime implements CasperSerializableObject, Tag {
    /** The Casper Version 1 Virtual Machine. */
    @JsonProperty("VmCasperV1")
    VM_CASPER_V1(0),
    /** The Casper Version 2 Virtual Machine. */
    @JsonProperty("VmCasperV2")
    VM_CASPER_V2(1);

    private final byte tag;

    TransactionRuntime(int tag) {
        this.tag = (byte) tag;
    }

    @Override
    public byte getByteTag() {
        return tag;
    }

    @Override
    public void serialize(SerializerBuffer ser, Target target) throws ValueSerializationException, NoSuchTypeException {
        ser.writeU8(getByteTag());
    }
}
