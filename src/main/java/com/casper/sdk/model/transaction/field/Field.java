package com.casper.sdk.model.transaction.field;

import com.casper.sdk.exception.NoSuchTypeException;
import com.casper.sdk.model.clvalue.serde.CasperSerializableObject;
import com.casper.sdk.model.clvalue.serde.Target;
import com.casper.sdk.model.transaction.NamedArgs;
import com.syntifi.crypto.key.encdec.Hex;
import dev.oak3.sbs4j.DeserializerBuffer;
import dev.oak3.sbs4j.SerializerBuffer;
import dev.oak3.sbs4j.exception.ValueDeserializationException;
import dev.oak3.sbs4j.exception.ValueSerializationException;
import dev.oak3.sbs4j.interfaces.DeserializableObject;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An indexed field with an offset and value.
 *
 * @author ian@meywood.com
 */
@NoArgsConstructor
@Getter
@Setter
public class Field implements CasperSerializableObject, DeserializableObject {

    private final Logger logger = LoggerFactory.getLogger(Field.class);


    /** The field index */
    private short index;
    /** The offset of the field's value when written to bytes */
    private long offset;
    /** The field value as bytes */
    private byte[] value;
    /** Indicates if the field is written as an optional field */
    private boolean optional;

    public Field(int index, long offset, byte[] value) {
        this.index = (short) index;
        this.offset = offset;
        this.value = value;
    }

    /**
     * Constructs a field with the specified index, offset and value.
     *
     * @param index  the index of the field
     * @param offset the offset of the fields bytes within a CalltableSerializationEnvelope
     * @param value  the value of the field
     * @throws ValueSerializationException if the value cannot be serialized
     */
    public Field(final int index, final long offset, final boolean optional, final Object value) throws ValueSerializationException, NoSuchTypeException {
        final SerializerBuffer serializerBuffer = new SerializerBuffer();

        // If optional indicate in 1sy byte
        if (optional) {
            serializerBuffer.writeBool(value != null);
        }

        if (value != null) {
            if (value instanceof NamedArgs) {
                // Named args in the enveloper builder are always byte target
                ((CasperSerializableObject) value).serialize(serializerBuffer, Target.BYTE);
            } else if (value instanceof CasperSerializableObject) {
                ((CasperSerializableObject) value).serialize(serializerBuffer);
            } else if (value instanceof Boolean) {
                serializerBuffer.writeBool((Boolean) value);
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
        }

        this.index = (short) index;
        this.offset = offset;
        this.optional = optional;
        this.value = serializerBuffer.toByteArray();
    }

    @Override
    public void serialize(final SerializerBuffer ser, final Target target) throws ValueSerializationException  {
        this.serialize(ser);
    }

    @Override
    public void serialize(final SerializerBuffer ser) throws ValueSerializationException {
        logger.debug("Serializing field index: {}, offset: {} {}, len: {}, value: {}", index, offset, String.format("0x%02X", offset), value.length, Hex.encode(value));
        ser.writeU16(index);
        ser.writeU32(offset);
    }

    @Override
    public void deserialize(final DeserializerBuffer deser) throws ValueDeserializationException {
        this.index = deser.readU16();
        this.offset = deser.readU32();
    }

    /**
     * Obtains the fields value converted to the specified type.
     *
     * @param clazz the type to convert the value's bytes to
     * @param <T>   the expected type
     * @return the bytes converted  to value of type clazz
     * @throws ValueDeserializationException if the value cannot be converted to the specified type
     */
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
        } else if (clazz == Boolean.class) {
            return (T) deserializerBuffer.readBool();
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
