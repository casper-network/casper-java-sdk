package com.casper.sdk.service.json.serialize;

import com.casper.sdk.types.CLByteArrayInfo;
import com.casper.sdk.types.CLOptionTypeInfo;
import com.casper.sdk.types.CLTypeInfo;
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
            } else if (value instanceof CLOptionTypeInfo) {
                gen.writeStartObject();
                gen.writeFieldName(value.getType().getJsonName());
                gen.writeString(((CLOptionTypeInfo) value).getInnerType().getType().getJsonName());
                gen.writeEndObject();
            } else {
                gen.writeString(value.getType().getJsonName());
            }
        }
    }
}
