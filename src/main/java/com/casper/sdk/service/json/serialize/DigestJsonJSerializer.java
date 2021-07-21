package com.casper.sdk.service.json.serialize;

import com.casper.sdk.types.Digest;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * Custom JSON Serialization class for {@link Digest} type object.
 */
public class DigestJsonJSerializer extends JsonSerializer<Digest> {

    @Override
    public void serialize(final Digest value,
                          final JsonGenerator gen,
                          final SerializerProvider serializers) throws IOException {

        if (value != null && value.getHash() != null) {
            gen.writeString(value.toString());
        }
    }
}
