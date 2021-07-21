package com.casper.sdk.service.json.serialize;

import com.casper.sdk.types.PublicKey;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * Custom JSON serializer for {@link PublicKey} type objects.
 */
public class PublicKeyJsonSerializer extends JsonSerializer<PublicKey> {

    @Override
    public void serialize(final PublicKey value,
                          final JsonGenerator gen,
                          final SerializerProvider serializers) throws IOException {
        if (value != null && value.getBytes() != null) {
            gen.writeString(value.toAccountHex());
        }
    }
}
