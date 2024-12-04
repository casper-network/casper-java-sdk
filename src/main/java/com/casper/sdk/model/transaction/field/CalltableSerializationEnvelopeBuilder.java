package com.casper.sdk.model.transaction.field;

import com.casper.sdk.exception.NoSuchTypeException;
import com.casper.sdk.model.clvalue.serde.CasperSerializableObject;
import com.casper.sdk.model.clvalue.serde.Target;
import dev.oak3.sbs4j.DeserializerBuffer;
import dev.oak3.sbs4j.SerializerBuffer;
import dev.oak3.sbs4j.exception.ValueDeserializationException;
import dev.oak3.sbs4j.exception.ValueSerializationException;
import dev.oak3.sbs4j.interfaces.DeserializableObject;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * The call table serialization envelope builder used to serialize and deserialize transaction fields/
 *
 * @author ian@meywood.com
 */
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class CalltableSerializationEnvelopeBuilder implements CasperSerializableObject, DeserializableObject {

    /** The fields to  serialize */
    private List<Field> fields = new ArrayList<>();
    /** The current field index */
    private long currentFieldIndex = -1;
    /** The expected number of fields */
    private long expectedFields;
    /** The offset of the current field om the serialized bytes */
    private long offset = 0;
    /** The total size of all field values when serialized */
    private long size = 0;

    /**
     * Add a field to the envelope.
     *
     * @param index the zero based index
     * @param value the value to be serialized to value bytes of the field
     */
    public <T> CalltableSerializationEnvelopeBuilder addField(final int index, final T value) throws ValueSerializationException {
        if (value != null) {
            return this.addField(new Field(index, this.offset, value));
        } else {
            return this;
        }
    }

    /**
     * Add a field to the envelope.
     *
     * @param index the zero based index
     * @param value the bytes of the value to add
     */
    public CalltableSerializationEnvelopeBuilder addFieldBytes(final int index, final byte[] value) {
        if (value != null) {
            return this.addField(new Field((short) index, offset, value));
        } else {
            return this;
        }
    }

    /**
     * Adds a field to the envelope.
     *
     * @param field the field to add
     */
    public CalltableSerializationEnvelopeBuilder addField(final Field field) {
        if (this.currentFieldIndex >= field.getIndex()) {
            throw new IllegalArgumentException("Field index must be greater than the previous field index");
        }

        if (this.currentFieldIndex + 1 != field.getIndex()) {
            throw new IllegalArgumentException("Field index must be sequential");
        }

        this.fields.add(field);
        this.currentFieldIndex = field.getIndex();
        this.size += field.getValue().length;
        this.offset += field.getValue().length;

        return this;
    }

    /**
     * Obtains the fields and converts its byte value to the specified type.
     *
     * @param index the index of the field
     * @param clazz the tye to convert the field value to
     * @param <T>   the type to convert the field value to
     * @return the field value as the specified type
     * @throws ValueDeserializationException if the field value cannot be converted to the specified type
     */
    public <T> T getFieldValue(final int index, final Class<T> clazz) throws ValueDeserializationException {
        final Field field = this.fields.get(index);
        return field.getValue(clazz);
    }

    /**
     * Obtains the field's value bytes.
     *
     * @param index the index of the field
     * @return the fields value bytes
     */
    public byte[] getFieldBytes(final int index) {
        return fields.get(index).getValue();
    }

    @Override
    public void serialize(final SerializerBuffer ser, final Target target) throws ValueSerializationException, NoSuchTypeException {

        if (this.fields.size() != this.expectedFields) {
            throw new IllegalArgumentException("Field index must be expected length " + this.expectedFields);
        }

        // Write the number of fields
        ser.writeI32(this.fields.size());

        for (final Field field : this.fields) {
            field.serialize(ser, target);
        }

        // Write total bytes of all field values
        ser.writeU32(this.size);
        for (final Field field : this.fields) {
            ser.writeByteArray(field.getValue());
        }
    }

    @Override
    public void deserialize(final DeserializerBuffer deserializerBuffer) throws ValueDeserializationException {

        final long numFields = deserializerBuffer.readU32();

        for (int i = 0; i < numFields; i++) {
            final Field field = new Field();
            field.deserialize(deserializerBuffer);
            this.fields.add(field);
        }

        this.size = deserializerBuffer.readU32();
        for (Field field : this.fields) {
            final long fieldValueLen = getFieldLength(field);
            field.setValue(deserializerBuffer.readByteArray((int) fieldValueLen));
        }
    }

    private long getFieldLength(final Field field) {
        if (field.getIndex() == this.fields.size() - 1) {
            return size - field.getOffset();
        } else {
            return this.fields.get(field.getIndex() + 1).getOffset() - field.getOffset();
        }
    }
}
