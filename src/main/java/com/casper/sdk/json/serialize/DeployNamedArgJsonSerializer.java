package com.casper.sdk.json.serialize;

import com.casper.sdk.domain.DeployNamedArg;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * Custom JSON serializer for a {@link DeployNamedArg} domain object, is written as an array
 * <pre>
 * [ "amount", { "cl_type": "U512", "bytes": "0400ca9a3b", "parsed": "1000000000" }]
 * </pre>
 */
public class DeployNamedArgJsonSerializer extends JsonSerializer<DeployNamedArg> {

    @Override
    public void serialize(final DeployNamedArg value,
                          final JsonGenerator gen,
                          final SerializerProvider serializers) throws IOException {
        // This is a bit funky is written as an array not an onkct
        gen.writeStartArray();
        gen.writeString(value.getName());
        gen.getCodec().writeValue(gen, value.getValue());
        gen.writeEndArray();
    }
}
