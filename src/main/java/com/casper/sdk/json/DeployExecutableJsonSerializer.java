package com.casper.sdk.json;

import com.casper.sdk.domain.CLValue;
import com.casper.sdk.domain.DeployExecutable;
import com.casper.sdk.domain.Payment;
import com.casper.sdk.domain.Transfer;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * Custom JSON serializer for all classes that extend the {@link DeployExecutable} domain object.
 */
public class DeployExecutableJsonSerializer extends JsonSerializer<DeployExecutable> {

    @Override
    public void serialize(final DeployExecutable value,
                          final JsonGenerator gen,
                          final SerializerProvider serializers) throws IOException {

        if (value instanceof Transfer) {
            writeTransfer((Transfer) value, gen);
        } else if (value instanceof Payment) {
            writePayment((Payment) value, gen);
        } else {
            writeArgs(value, gen);
        }
    }

    private void writePayment(final Payment payment, final JsonGenerator gen) throws IOException {
        gen.writeStartObject();
        gen.writeFieldName("ModuleBytes");
        gen.writeStartObject();
        gen.writeFieldName("module_bytes");
        gen.writeString(CLValue.toHex(payment.getModuleBytes()));
        writeArgs(payment, gen);
        gen.writeEndObject();
        gen.writeEndObject();
    }

    private void writeTransfer(final Transfer transfer,
                               final JsonGenerator gen) throws IOException {
        gen.writeStartObject();
        gen.writeFieldName("Transfer");
        gen.writeStartObject();
        writeArgs(transfer, gen);
        gen.writeEndObject();
        gen.writeEndObject();
    }

    private void writeArgs(DeployExecutable value, JsonGenerator gen) throws IOException {
        gen.writeFieldName("args");
        gen.getCodec().writeValue(gen, value.getArgs());
    }
}
