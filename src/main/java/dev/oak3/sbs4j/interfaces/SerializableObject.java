package dev.oak3.sbs4j.interfaces;

import dev.oak3.sbs4j.SerializerBuffer;
import dev.oak3.sbs4j.exception.ValueSerializationException;

/**
 * Defines an object as being capable of deserializing with {@link SerializerBuffer}
 *
 * @since 0.1.0
 */
public interface SerializableObject {
    /**
     * Called when the object's values must be serialized
     *
     * @param serializerBuffer the serializer buffer to be used
     * @throws ValueSerializationException exception holding information of failure to serialize a value
     */
    void serialize(SerializerBuffer serializerBuffer) throws ValueSerializationException;
}
