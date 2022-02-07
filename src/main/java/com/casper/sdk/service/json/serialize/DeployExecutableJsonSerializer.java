package com.casper.sdk.service.json.serialize;

import com.casper.sdk.types.*;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * Custom JSON serializer for all classes that extend the {@link DeployExecutable} type object.
 */
public class DeployExecutableJsonSerializer extends JsonSerializer<DeployExecutable> {

    @Override
    public void serialize(final DeployExecutable value,
                          final JsonGenerator gen,
                          final SerializerProvider serializers) throws IOException {

        if (value instanceof Transfer) {
            writeTransfer((Transfer) value, gen);
        } else if (value instanceof StoredContractByHash) {
            writeStoredContractByHash((StoredContractByHash) value, gen);
        } else if (value instanceof StoredVersionedContractByName) {
            writeStoredVersionedContractByName((StoredVersionedContractByName) value, gen);
        } else if (value instanceof StoredContractByName) {
            writeStoredContractByName((StoredContractByName) value, gen);
        } else if (value instanceof ModuleBytes) {
            writePayment((ModuleBytes) value, gen);
        } else {
            writeArgs(value, gen);
        }
    }

    private void writePayment(final ModuleBytes payment, final JsonGenerator gen) throws IOException {
        gen.writeStartObject();
        gen.writeFieldName("ModuleBytes");
        gen.writeStartObject();
        gen.writeFieldName("module_bytes");
        gen.writeString(CLValue.toHex(payment.getModuleBytes()));
        writeArgs(payment, gen);
        gen.writeEndObject();
        gen.writeEndObject();
    }

    private void writeStoredContractByHash(final StoredContractByHash storedContractByHash,
                                           final JsonGenerator gen) throws IOException {
        gen.writeStartObject();
        gen.writeFieldName("StoredContractByHash");
        gen.writeStartObject();
        gen.writeFieldName("hash");
        gen.writeString(storedContractByHash.getHash().toString());
        gen.writeFieldName("entry_point");
        gen.writeString(storedContractByHash.getEntryPoint());
        writeArgs(storedContractByHash, gen);
        gen.writeEndObject();
        gen.writeEndObject();
    }

    private void writeStoredContractByName(final StoredContractByName storedContractByName,
                                           final JsonGenerator gen) throws IOException {
        gen.writeStartObject();
        gen.writeFieldName("StoredContractByName");
        gen.writeStartObject();
        gen.writeFieldName("name");
        gen.writeString(storedContractByName.getName());
        gen.writeFieldName("entry_point");
        gen.writeString(storedContractByName.getEntryPoint());
        writeArgs(storedContractByName, gen);
        gen.writeEndObject();
        gen.writeEndObject();
    }

    private void writeStoredVersionedContractByName(final StoredVersionedContractByName storedVersionedContractByName,
                                                    final JsonGenerator gen) throws IOException {
        gen.writeStartObject();
        gen.writeFieldName("StoredVersionedContractByName");
        gen.writeStartObject();
        gen.writeFieldName("name");
        gen.writeString(storedVersionedContractByName.getName());

        if (storedVersionedContractByName.getVersion().isPresent()) {
            gen.writeFieldName("version");
            gen.writeNumber(storedVersionedContractByName.getVersion().get().longValue());
        }

        gen.writeFieldName("entry_point");
        gen.writeString(storedVersionedContractByName.getEntryPoint());
        writeArgs(storedVersionedContractByName, gen);
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

    private void writeArgs(final DeployExecutable value, final JsonGenerator gen) throws IOException {
        gen.writeFieldName("args");
        gen.getCodec().writeValue(gen, value.getArgs());
    }
}
