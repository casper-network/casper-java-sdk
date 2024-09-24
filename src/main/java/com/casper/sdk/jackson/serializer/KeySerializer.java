package com.casper.sdk.jackson.serializer;

import com.casper.sdk.model.key.Key;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * Desrializer for {@link Key} types.
 *
 * @author ian@meywood.com
 */
public class KeySerializer extends JsonSerializer<Key> {
    @Override
    public void serialize(final Key key, final JsonGenerator gen, final SerializerProvider serializers) throws IOException {
        gen.writeString(key.toString());
    }
}
