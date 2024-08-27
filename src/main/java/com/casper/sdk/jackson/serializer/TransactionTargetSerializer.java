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
            gen.writeStartObject();
            gen.writeFieldName("Session");
            gen.writeStartObject();
            gen.writeStringField("module_bytes", Hex.encode(((Session) value).getModuleBytes()));
            gen.writeStringField("runtime", TransactionRuntime.getJsonName(((Session) value).getRuntime()));
            gen.writeEndObject();
            gen.writeEndObject();
        } else if (value instanceof Native) {
            gen.writeString("Native");
        } else if (value instanceof Stored){
            //TODO
            throw new IllegalArgumentException("Stored is not yet implemented");
        } else {
            throw new IllegalArgumentException("Unknown transaction target type: " + value.getClass().getName());
        }
    }

}
