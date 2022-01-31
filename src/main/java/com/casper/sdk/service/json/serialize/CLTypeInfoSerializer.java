package com.casper.sdk.service.json.serialize;

import com.casper.sdk.types.*;
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
            } else if (value instanceof CLMapTypeInfo) {
                gen.writeStartObject();
                gen.writeFieldName(value.getType().getJsonName());
                gen.writeStartObject();
                gen.writeFieldName("key");
                gen.getCodec().writeValue(gen, ((CLMapTypeInfo)value).getKeyType());
                gen.writeFieldName("value");
                gen.getCodec().writeValue(gen, ((CLMapTypeInfo)value).getValueType());
                gen.writeEndObject();
                gen.writeEndObject();
            } else {
                gen.writeString(value.getType().getJsonName());
            }
        }
    }
}
