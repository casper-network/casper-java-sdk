package com.casper.sdk.model.transaction.field;

import com.casper.sdk.exception.NoSuchTypeException;
import com.casper.sdk.model.clvalue.serde.CasperSerializableObject;
import com.casper.sdk.model.clvalue.serde.Target;
import dev.oak3.sbs4j.DeserializerBuffer;
import dev.oak3.sbs4j.SerializerBuffer;
import dev.oak3.sbs4j.exception.ValueDeserializationException;
import dev.oak3.sbs4j.exception.ValueSerializationException;
import dev.oak3.sbs4j.interfaces.DeserializableObject;
import dev.oak3.sbs4j.interfaces.SerializableObject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * An indexed field with an offset and value.
 *
 * @author ian@meywood.com
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Field implements CasperSerializableObject, DeserializableObject {

    /** The field index */
    private short index;
    /** The offset of the field's value when written to bytes */
    private long offset;
    /** The field value as bytes */
    private byte[] value;

    public Field(final int index, final long offset, final Object value) throws ValueSerializationException {
        final SerializerBuffer serializerBuffer = new SerializerBuffer();

        if (value instanceof SerializableObject) {
            ((SerializableObject) value).serialize(serializerBuffer);
        } else if (value instanceof byte[]) {
            serializerBuffer.writeByteArray((byte[]) value);
        } else if (value.getClass().equals(Byte.class)) {
            serializerBuffer.writeU8((Byte) value);
        } else if (value.getClass().equals(Long.class)) {
            serializerBuffer.writeI64((Long) value);
        } else if (value.getClass().equals(Integer.class)) {
            serializerBuffer.writeI32((Integer) value);
        } else if (value.getClass().equals(Short.class)) {
            serializerBuffer.writeU16((Short) value);
        } else if (value.getClass().equals(String.class)) {
            serializerBuffer.writeString((String) value);
        } else {
            throw new ValueSerializationException("Unsupported type " + value.getClass().getName());
        }

        this.index = (short) index;
        this.offset = offset;
        this.value = serializerBuffer.toByteArray();
    }

    @Override
    public void serialize(final SerializerBuffer ser, final Target target) throws ValueSerializationException, NoSuchTypeException {
        this.serialize(ser);
    }

    @Override
    public void serialize(final SerializerBuffer ser) throws ValueSerializationException {
        ser.writeU16(index);
        ser.writeU32(offset);
    }

    @Override
    public void deserialize(final DeserializerBuffer deser) throws ValueDeserializationException {
        this.index = deser.readU16();
        this.offset = deser.readU32();
    }

    @SuppressWarnings("unchecked")
    public <T> T getValue(final Class<T> clazz) throws ValueDeserializationException {
        final DeserializerBuffer deserializerBuffer = new DeserializerBuffer(this.value);

        if (clazz.isAssignableFrom(DeserializableObject.class)) {
            try {
                final T value = clazz.getDeclaredConstructor().newInstance();
                ((DeserializableObject) value).deserialize(deserializerBuffer);
                return value;
            } catch (Exception e) {
                throw new ValueDeserializationException("Unsupported type " + clazz.getName(), e);
            }
        } else if (clazz == byte[].class) {
            return (T) deserializerBuffer.readByteArray(this.value.length);
        } else if (clazz == Byte.class) {
            return (T) (Byte) deserializerBuffer.readU8();
        } else if (clazz == Long.class) {
            return (T) (Long) deserializerBuffer.readU32();
        } else if (clazz == Integer.class) {
            return (T) (Integer) deserializerBuffer.readI32();
        } else if (clazz == Short.class) {
            return (T) (Short) deserializerBuffer.readU16();
        } else if (clazz == String.class) {
            return (T) deserializerBuffer.readString();
        }

        throw new IllegalArgumentException("Unsupported type " + clazz.getName());
    }
}
