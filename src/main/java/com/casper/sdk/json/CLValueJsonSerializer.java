package com.casper.sdk.json;

import com.casper.sdk.domain.CLValue;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * Custom JSON serializer for {@link CLValue} domain objects
 */
public class CLValueJsonSerializer extends JsonSerializer<CLValue> {

    @Override
    public void serialize(final CLValue value,
                          final JsonGenerator gen,
                          final SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        gen.writeFieldName("cl_type");
        gen.getCodec().writeValue(gen, value.getCLTypeInfo());
        gen.writeFieldName("bytes");
        gen.writeString(value.toHex());
        writeParsed(value, gen);
        gen.writeEndObject();
    }

    private void writeParsed(CLValue value, JsonGenerator gen) throws IOException {
        if (value.getParsed() != null) {
            gen.writeFieldName("parsed");
            if (value.getParsed() instanceof Number) {
                gen.writeNumber(((Number) value.getParsed()).intValue());
            } else {
                gen.writeString(value.getParsed().toString());
            }
        }
    }
}
