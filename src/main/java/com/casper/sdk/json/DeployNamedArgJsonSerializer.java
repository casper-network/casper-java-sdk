package com.casper.sdk.json;

import com.casper.sdk.domain.DeployNamedArg;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * Custom JSON serializer for a {@link DeployNamedArg} domain object. "amount", { "cl_type": "U512", "bytes":
 * "0400ca9a3b", "parsed": "1000000000" }
 */
public class DeployNamedArgJsonSerializer extends JsonSerializer<DeployNamedArg> {

    @Override
    public void serialize(final DeployNamedArg value,
                          final JsonGenerator gen,
                          final SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        gen.writeFieldName(value.getName());
        gen.getCodec().writeValue(gen, value.getValue());
        gen.writeEndObject();
    }
}
