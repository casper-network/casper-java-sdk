package com.casper.sdk.model.clvalue.serde;

import com.casper.sdk.exception.NoSuchTypeException;
import dev.oak3.sbs4j.SerializerBuffer;
import dev.oak3.sbs4j.exception.ValueSerializationException;
import dev.oak3.sbs4j.interfaces.SerializableObject;

/**
 * Defines an object as being capable of encoding with {@link SerializerBuffer}
 *
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.2.2
 */
public interface CasperSerializableObject extends SerializableObject {
    /**
     * Called when the object's values must be serialized
     *
     * @param ser    the encoder to be used
     * @param target target serialization standard
     * @throws ValueSerializationException exception holding information of failure to serialize a value
     */
    void serialize(SerializerBuffer ser, Target target) throws ValueSerializationException, NoSuchTypeException;

    /**
     * Called when the object's values must be serialized
     * <p>
     * Allows to use the default serialize with the custom casper serialize signature, defaulting encodeType to false
     *
     * @param ser the encoder to be used
     * @throws ValueSerializationException exception holding information of failure to serialize a value
     */
    @Override
    default void serialize(SerializerBuffer ser) throws ValueSerializationException {
        try {
            serialize(ser, Target.JSON);
        } catch (NoSuchTypeException e) {
            throw new ValueSerializationException(String.format("Error serializing %s", this.getClass().getSimpleName()), e);
        }
    }
}
