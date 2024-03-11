package com.casper.sdk.model.clvalue;

import com.casper.sdk.annotation.ExcludeFromJacocoGeneratedReport;
import com.casper.sdk.exception.NoSuchTypeException;
import com.casper.sdk.model.clvalue.cltype.AbstractCLTypeWithChildren;
import com.casper.sdk.model.clvalue.cltype.CLTypeData;
import com.fasterxml.jackson.annotation.JsonSetter;
import dev.oak3.sbs4j.DeserializerBuffer;
import dev.oak3.sbs4j.SerializerBuffer;
import dev.oak3.sbs4j.exception.ValueDeserializationException;
import lombok.EqualsAndHashCode;
import lombok.SneakyThrows;

/**
 * Abstract class for those CLValues which have a child collection
 *
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @see AbstractCLValue
 * @since 0.0.1
 */
@EqualsAndHashCode(callSuper = true)
public abstract class AbstractCLValueWithChildren<T, P extends AbstractCLTypeWithChildren> extends AbstractCLValue<T, P> {
    protected abstract void setChildTypes(T value);

    /**
     * This fires deserialization in case the json/jackson mapped bytes before CLType
     * This only happens when whe have CLTypes with children types.
     * <p>
     * Should always be called in the end of the json setter for ClType within CLValueWithChildren
     */
    @SneakyThrows({ValueDeserializationException.class})
    protected void childTypesSet() {
        if (!getBytes().isEmpty()) {
            this.deserialize(new DeserializerBuffer(this.getBytes()));
        }
    }

    /**
     * Sets the bytes and if the CLType is already set, fires bytes deserialization
     *
     * @param bytes the input bytes for this CLValue
     */
    @Override
    @SneakyThrows({ValueDeserializationException.class})
    @JsonSetter(value = "bytes")
    @ExcludeFromJacocoGeneratedReport
    protected void setJsonBytes(String bytes) {
        this.setBytes(bytes);

        if (!getClType().getChildTypes().isEmpty() && getClType().isDeserializable()) {
            this.deserialize(new DeserializerBuffer(this.getBytes()));
        }
    }

    protected void encodeType(final SerializerBuffer ser) throws NoSuchTypeException {
        super.encodeType(ser);
        encodeChildTypes(ser);
    }

    protected abstract void encodeChildTypes(final SerializerBuffer ser) throws NoSuchTypeException;

    /**
     * Encodes the bytes of the child type, if the child value is not present but the type is known from the parent type
     * info, the childDataType is used to encode the bytes of the child rather than the child value.
     *
     * @param ser           the serializer buffer
     * @param child         the child value whose type is to be encoded
     * @param childDataType the data type of the child
     * @throws NoSuchTypeException if the child type is not found
     */
    protected void encodeChildType(final SerializerBuffer ser,
                                   final AbstractCLValue<?, ?> child,
                                   final CLTypeData childDataType) throws NoSuchTypeException {
        if (child instanceof AbstractCLValueWithChildren) {
            child.encodeType(ser);
        } else {
            // If there are no AbstractCLValueWithChildren as children we just need a simple tag
            byte element0TypeTag = childDataType.getSerializationTag();
            ser.writeU8(element0TypeTag);
        }
    }

    public void populateChildTypes(final DeserializerBuffer dser) {

    }
}
