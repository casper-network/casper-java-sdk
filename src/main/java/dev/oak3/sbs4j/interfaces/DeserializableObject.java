package dev.oak3.sbs4j.interfaces;

import dev.oak3.sbs4j.DeserializerBuffer;
import dev.oak3.sbs4j.exception.ValueDeserializationException;

/**
 * Defines an object as being capable of deserializing with {@link DeserializerBuffer}
 *
 * @since 0.1.0
 */
public interface DeserializableObject {
    /**
     * Called when the object's values must be deserialized
     *
     * @param deserializerBuffer the deserializer buffer to be used
     * @throws ValueDeserializationException exception holding information of failure to deserialize a value
     */
    void deserialize(DeserializerBuffer deserializerBuffer) throws ValueDeserializationException;
}
