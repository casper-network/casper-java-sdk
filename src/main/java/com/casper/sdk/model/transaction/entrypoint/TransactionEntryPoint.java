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

    protected static final int CUSTOM_TAG = 0;
    protected static final int TRANSFER_TAG = 1;
    protected static final int ADD_BID_TAG = 2;
    protected static final int WITHDRAW_BID_TAG = 3;
    protected static final int DELEGATE_TAG = 4;
    protected static final int UNDELEGATE_TAG = 5;
    protected static final int REDELEGATE_TAG = 6;
    protected static final int ACTIVATE_BID_TAG = 7;
    protected static final int CHANGE_BID_PUBLIC_KEY_TAG = 8;
    protected static final int CALL_TAG = 9;


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
