package com.casper.sdk.model.transaction;

import com.casper.sdk.exception.NoSuchTypeException;
import com.casper.sdk.model.clvalue.serde.CasperSerializableObject;
import com.casper.sdk.model.clvalue.serde.Target;
import com.casper.sdk.model.key.Tag;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonValue;
import dev.oak3.sbs4j.SerializerBuffer;
import dev.oak3.sbs4j.exception.ValueSerializationException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The address of the transaction initiator.
 *
 * @author ian@meywood.com
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
@JsonSubTypes({
        @JsonSubTypes.Type(value = InitiatorPublicKey.class, name = "PublicKey"),
        @JsonSubTypes.Type(value = InitiatorAccountHash.class, name = "AccountHash")})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public abstract class InitiatorAddr<T> implements CasperSerializableObject, Tag {

    protected static final int PUBLIC_KEY_TAG = 0;
    protected static final int ACCOUNT_HASH_TAG = 1;

    @JsonValue
    private T address;

    @Override
    public void serialize(final SerializerBuffer ser, final Target target) throws ValueSerializationException, NoSuchTypeException {
        ser.writeU8(getByteTag());
        ((CasperSerializableObject) getAddress()).serialize(ser, target);
    }
}
