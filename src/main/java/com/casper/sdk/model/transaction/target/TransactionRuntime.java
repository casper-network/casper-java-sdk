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
    VM_CASPER_V1(0, "VmCasperV1"),
    /** The Casper Version 2 Virtual Machine. */
    @JsonProperty("VmCasperV2")
    VM_CASPER_V2(1, "VmCasperV2");

    private final byte tag;
    private final String jsonName;

    TransactionRuntime(final int tag, final String jsonName) {
        this.tag = (byte) tag;
        this.jsonName = jsonName;
    }

    @Override
    public byte getByteTag() {
        return tag;
    }

    public static String getJsonName(final TransactionRuntime runtime) {
        return runtime.jsonName;
    }

    public static TransactionRuntime getJsonRuntime(final String name) throws NoSuchTypeException {
        for (TransactionRuntime t: values()){
            if (t.jsonName.equals(name)){
                return t;
            }
        }
        throw new NoSuchTypeException(name);
    }

    @Override
    public void serialize(final SerializerBuffer ser, final Target target) throws ValueSerializationException, NoSuchTypeException {
        ser.writeU8(getByteTag());
    }
}
