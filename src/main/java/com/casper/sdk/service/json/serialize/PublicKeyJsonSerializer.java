package com.casper.sdk.service.json.serialize;

import com.casper.sdk.types.CLPublicKey;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * Custom JSON serializer for {@link CLPublicKey} type objects.
 */
public class PublicKeyJsonSerializer extends JsonSerializer<CLPublicKey> {

    @Override
    public void serialize(final CLPublicKey value,
                          final JsonGenerator gen,
                          final SerializerProvider serializers) throws IOException {
        if (value != null && value.getBytes() != null) {
            gen.writeString(value.toAccountHex());
        }
    }
}
