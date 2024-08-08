package com.casper.sdk.jackson.serializer;

import com.casper.sdk.exception.DeserializationException;
import com.casper.sdk.model.transaction.target.Session;
import com.casper.sdk.model.transaction.target.TransactionRuntime;
import com.casper.sdk.model.transaction.target.TransactionTarget;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.syntifi.crypto.key.encdec.Hex;

import java.io.IOException;

/**
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
            try {
                gen.writeStringField("runtime", getTransactionRuntime(((Session) value).getRuntime()));
            } catch (NoSuchFieldException e) {
                throw new DeserializationException(e.getMessage(), e);
            }
            gen.writeEndObject();
            gen.writeEndObject();
        }

    }

    private static String getTransactionRuntime(TransactionRuntime value) throws NoSuchFieldException {
        return value.getClass().getDeclaredField(value.name()).getAnnotation(JsonProperty.class).value();
    }


}
