package com.casper.sdk.model.transaction.target;

import com.casper.sdk.exception.NoSuchTypeException;
import com.casper.sdk.model.clvalue.serde.CasperSerializableObject;
import com.casper.sdk.model.clvalue.serde.Target;
import com.casper.sdk.model.key.Tag;
import com.casper.sdk.model.transaction.field.CalltableSerializationEnvelopeBuilder;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import dev.oak3.sbs4j.SerializerBuffer;
import dev.oak3.sbs4j.exception.ValueSerializationException;
import lombok.Getter;

/**
 * The runtime used to execute a `Transaction`.
 *
 * TODO refactor into classes as VmCasperV2 now has fields transferred_value and seed FFS
 * @author ian@meywood.com
 */
@Getter
public enum TransactionRuntime implements CasperSerializableObject, Tag {

    /** The Casper Version 1 Virtual Machine. */
    @JsonProperty("VmCasperV1")
    VM_CASPER_V1(0, "VmCasperV1"),
    /** The Casper Version 2 Virtual Machine. */
    @JsonProperty("VmCasperV2")
    VM_CASPER_V2(1, "VmCasperV2");

    private static final int TAG_FIELD_INDEX = 0;

    private final byte tag;
    private final String jsonName;

    TransactionRuntime(final int tag, final String jsonName) {
        this.tag = (byte) tag;
        this.jsonName = jsonName;
    }

    @JsonIgnore
    @Override
    public byte getByteTag() {
        return tag;
    }

    public static String toJson(final TransactionRuntime runtime) {
        return runtime != null ? runtime.jsonName : null;
    }

    public static TransactionRuntime fromJson(final String name) throws NoSuchTypeException {
        for (TransactionRuntime t : values()) {
            if (t.jsonName.equals(name)) {
                return t;
            }
        }
        throw new NoSuchTypeException(name);
    }

    @Override
    public void serialize(final SerializerBuffer ser, final Target target) throws ValueSerializationException, NoSuchTypeException {
        new CalltableSerializationEnvelopeBuilder(target)
                .addField(TAG_FIELD_INDEX, getByteTag())
                .serialize(ser, target);

        // TODO need to convert to object as VmCasperV2 now has fields transferred_value and seed

    }
}
