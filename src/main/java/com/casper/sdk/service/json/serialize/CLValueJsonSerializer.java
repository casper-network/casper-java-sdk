package com.casper.sdk.service.json.serialize;

import com.casper.sdk.types.CLMap;
import com.casper.sdk.types.CLValue;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.util.Map;

/**
 * Custom JSON serializer for {@link CLValue} type objects
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
                gen.writeNumber(value.getParsed().toString());
            } else if (value instanceof CLMap) {
                writeMap((CLMap) value, gen);
            } else {
                gen.writeString(value.getParsed().toString());
            }
        }
    }

    private void writeMap(final CLMap value, final JsonGenerator gen) throws IOException {

        gen.writeStartArray();

        for (Map.Entry<CLValue, CLValue> entry : value.entrySet()) {

            gen.writeStartObject();
            gen.writeFieldName("key");
            gen.getCodec().writeValue(gen, entry.getKey().getParsed());
            gen.writeFieldName("value");
            gen.getCodec().writeValue(gen, entry.getValue().getParsed());
            gen.writeEndObject();
        }

        gen.writeEndArray();
    }
}
