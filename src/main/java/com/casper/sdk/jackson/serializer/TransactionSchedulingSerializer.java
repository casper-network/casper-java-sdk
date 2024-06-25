package com.casper.sdk.jackson.serializer;

import com.casper.sdk.model.transaction.scheduling.FutureEra;
import com.casper.sdk.model.transaction.scheduling.FutureTimestamp;
import com.casper.sdk.model.transaction.scheduling.Standard;
import com.casper.sdk.model.transaction.scheduling.TransactionScheduling;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * Serializes {@link TransactionScheduling} objects.
 *
 * @author ian@meywood.com
 */
public class TransactionSchedulingSerializer extends JsonSerializer<TransactionScheduling> {
    @Override
    public void serialize(final TransactionScheduling value,
                          final JsonGenerator gen,
                          final SerializerProvider serializers) throws IOException {
        if (value instanceof Standard) {
            gen.writeString("Standard");
        } else if (value instanceof FutureTimestamp) {
            gen.writeStartObject();
            gen.writeStringField(FutureTimestamp.class.getSimpleName(), ((FutureTimestamp) value).getFutureTimestamp());
            gen.writeEndObject();
        } else if (value instanceof FutureEra) {
            gen.writeStartObject();
            gen.writeStringField(FutureEra.class.getSimpleName(), ((FutureEra) value).getEraId().toString());
            gen.writeEndObject();
        } else {
            throw new IllegalArgumentException("Unknown scheduling type: " + value.getClass().getName());
        }
    }
}
