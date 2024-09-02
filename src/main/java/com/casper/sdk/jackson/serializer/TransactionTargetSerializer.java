package com.casper.sdk.jackson.serializer;

import com.casper.sdk.model.transaction.target.*;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.syntifi.crypto.key.encdec.Hex;

import java.io.IOException;

/**
 * Serializes {@link TransactionTarget} objects.
 *
 * @author carl@stormeye.co.uk
 */
public class TransactionTargetSerializer extends JsonSerializer<TransactionTarget> {

    @Override
    public void serialize(final TransactionTarget value, final JsonGenerator gen, final SerializerProvider serializers) throws IOException {

        if (value instanceof Session) {
            serializeSession((Session) value, gen);
        } else if (value instanceof Native) {
            gen.writeString("Native");
        } else if (value instanceof Stored) {
            serializeStored((Stored) value, gen);
        } else {
            throw new IllegalArgumentException("Unknown transaction target type: " + value.getClass().getName());
        }
    }

    private static void serializeStored(final Stored value, final JsonGenerator gen) throws IOException {
        gen.writeStartObject();
        gen.writeFieldName("Stored");
        gen.writeStartObject();
        gen.writeFieldName("id");
        gen.writeObject(value.getId());
        if (value.getRuntime() != null) {
            gen.writeFieldName("runtime");
            gen.writeString(value.getRuntime().getJsonName());
        }
        gen.writeEndObject();
        gen.writeEndObject();
    }

    private static void serializeSession(final Session value, final JsonGenerator gen) throws IOException {
        gen.writeStartObject();
        gen.writeFieldName("Session");
        gen.writeStartObject();
        gen.writeStringField("module_bytes", Hex.encode(value.getModuleBytes()));
        gen.writeStringField("runtime", TransactionRuntime.toJson(value.getRuntime()));
        gen.writeEndObject();
        gen.writeEndObject();
    }

}
