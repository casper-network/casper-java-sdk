package com.casper.sdk.json.serialize;

import com.casper.sdk.domain.CLByteArrayInfo;
import com.casper.sdk.domain.CLTypeInfo;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * JSON serializer for a {@link CLTypeInfo}
 */
public class CLTypeInfoSerializer extends JsonSerializer<CLTypeInfo> {

    @Override
    public void serialize(final CLTypeInfo value,
                          final JsonGenerator gen,
                          final SerializerProvider serializers) throws IOException {
        if (value != null) {

            if (value instanceof CLByteArrayInfo) {
                gen.writeStartObject();
                gen.writeFieldName(value.getType().getJsonName());
                gen.writeNumber(((CLByteArrayInfo) value).getSize());
                gen.writeEndObject();
            } else {
                gen.writeString(value.getType().getJsonName());
            }
        }
    }
}
