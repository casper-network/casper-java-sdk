package com.casper.sdk.model.clvalue;

import com.casper.sdk.annotation.ExcludeFromJacocoGeneratedReport;
import com.casper.sdk.model.clvalue.cltype.AbstractCLTypeWithChildren;
import com.fasterxml.jackson.annotation.JsonSetter;
import dev.oak3.sbs4j.DeserializerBuffer;
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
}
