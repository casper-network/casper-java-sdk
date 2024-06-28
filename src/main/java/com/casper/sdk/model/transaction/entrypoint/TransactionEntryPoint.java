package com.casper.sdk.model.transaction.entrypoint;

import com.casper.sdk.jackson.deserializer.TransactionEntryPointDeserializer;
import com.casper.sdk.jackson.serializer.TransactionEntryPointSerializer;
import com.casper.sdk.model.clvalue.serde.CasperSerializableObject;
import com.casper.sdk.model.clvalue.serde.Target;
import com.casper.sdk.model.key.Tag;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import dev.oak3.sbs4j.SerializerBuffer;
import dev.oak3.sbs4j.exception.ValueSerializationException;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * The entry point of a Transaction.
 *
 * @author ian@meywood.com
 */
@AllArgsConstructor
@NoArgsConstructor(force = true)
@JsonSerialize(using = TransactionEntryPointSerializer.class)
@JsonDeserialize(using = TransactionEntryPointDeserializer.class)
@EqualsAndHashCode(callSuper = false)
@Getter
public abstract class TransactionEntryPoint implements CasperSerializableObject, Tag {

    private final byte tag;
    private final String name;

    @Override
    public byte getByteTag() {
        return tag;
    }

    @Override
    public void serialize(final SerializerBuffer ser, final Target target) throws ValueSerializationException {
        ser.writeU8(getByteTag());
    }

    @Override
    public String toString() {
        return name;
    }
}
