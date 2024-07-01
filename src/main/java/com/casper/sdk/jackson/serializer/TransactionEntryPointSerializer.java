package com.casper.sdk.jackson.serializer;

import com.casper.sdk.model.transaction.entrypoint.CustomEntryPoint;
import com.casper.sdk.model.transaction.entrypoint.TransactionEntryPoint;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * Transaction entry point JSON serializer, needed to match rust nested enums JSON serialization.
 *
 * @author ian@meywood.com
 */
public class TransactionEntryPointSerializer extends JsonSerializer<TransactionEntryPoint> {
    @Override
    public void serialize(final TransactionEntryPoint value,
                          final JsonGenerator gen,
                          final SerializerProvider serializers) throws IOException {

        if (value instanceof CustomEntryPoint) {
            gen.writeStartObject();
            gen.writeStringField(value.getName(), ((CustomEntryPoint) value).getCustom());
            gen.writeEndObject();
        } else {
            gen.writeString(value.getName());
        }
    }
}
