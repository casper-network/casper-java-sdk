package com.casper.sdk.jackson.serializer;

import com.casper.sdk.model.key.Key;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.type.WritableTypeId;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;

import java.io.IOException;

/**
 * Deserializer for {@link Key} types.
 *
 * @author ian@meywood.com
 */
public class KeySerializer extends JsonSerializer<Key> {
    @Override
    public void serialize(final Key key, final JsonGenerator gen, final SerializerProvider serializers) throws IOException {
        gen.writeString(key.toString());
    }

    @Override
    public void serializeWithType(final Key value,
                                  final JsonGenerator gen,
                                  final SerializerProvider serializers,
                                  final TypeSerializer typeSer) throws IOException {
        final WritableTypeId writableTypeId = typeSer.typeId(value, JsonToken.VALUE_EMBEDDED_OBJECT);
        typeSer.writeTypePrefix(gen, writableTypeId);
        gen.writeString(value.toString());
        typeSer.writeTypeSuffix(gen, writableTypeId);
    }
}
