package com.casper.sdk.model.clvalue.serde;

import com.casper.sdk.exception.NoSuchTypeException;
import com.casper.sdk.model.clvalue.AbstractCLValue;
import dev.oak3.sbs4j.DeserializerBuffer;
import dev.oak3.sbs4j.exception.ValueDeserializationException;
import dev.oak3.sbs4j.interfaces.DeserializableObject;

/**
 * Defines an object as being capable of encoding with {@link DeserializerBuffer}
 *
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.2.2
 */
public interface CasperDeserializableObject extends DeserializableObject {
    /**
     * Called when the object's values must be deserialized
     *
     * @param deser  the deserializer to be used
     * @param target target deserialization standard
     * @throws ValueDeserializationException exception holding information of failure to deserialize a value
     */
    AbstractCLValue<?, ?> deserialize(DeserializerBuffer deser, Target target) throws ValueDeserializationException, NoSuchTypeException;

    /**
     * Called when the object's values must be deserialized
     * <p>
     * Allows to use the default deserialize with the custom casper deserialize signature, defaulting encodeType to false
     *
     * @param deser the deserializer to be used
     * @throws ValueDeserializationException exception holding information of failure to deserialize a value
     */
    @Override
    default void deserialize(DeserializerBuffer deser) throws ValueDeserializationException {
        try {
            deserialize(deser, Target.JSON);
        } catch (NoSuchTypeException e) {
            throw new ValueDeserializationException(String.format("Error deserializing %s", this.getClass().getSimpleName()), e);
        }
    }
}
