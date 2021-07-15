package com.casper.sdk.service;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.StringWriter;

public class TransferService {

    public String buildJson() throws IOException {
        return generateJson();
    }

    private String generateJson() throws IOException {

        final StringWriter writer = new StringWriter();
        final JsonGenerator generator =  new ObjectMapper().createGenerator(writer);

        generator.writeStartObject();
        generator.writeFieldName("deploy");

        generator.writeStartObject();
        generator.writeFieldName("approvals");
        generator.writeStartArray();
        generator.writeStartObject();
        generator.writeStringField("signature", "123");
        generator.writeStringField("signer", "456");
        generator.writeEndObject();
        generator.writeEndArray();
        generator.writeEndObject();

        generator.writeStringField("hash", "133");

        generator.writeFieldName("header");
        generator.writeStartObject();
        generator.writeStringField("account", "abc");
        generator.writeStringField("body_hash", "abc");
        generator.writeStringField("chain_name", "abc");
        generator.writeFieldName("dependencies");
        generator.writeStartArray();
        generator.writeEndArray();
        generator.writeStringField("gas_price", "abc");
        generator.writeStringField("timestamp", "abc");
        generator.writeStringField("ttl", "abc");
        generator.writeEndObject();


        generator.writeFieldName("payment");
        generator.writeStartObject();
        generator.writeFieldName("ModuleBytes");
        generator.writeStartObject();
        generator.writeFieldName("args");
        generator.writeStartArray();
        generator.writeStartArray();
        generator.writeString("amount");
        generator.writeStartObject();
        generator.writeStringField("bytes", "abc");
        generator.writeStringField("cl_type", "abc");
        generator.writeStringField("parsed", "abc");
        generator.writeEndObject();
        generator.writeEndArray();
        generator.writeEndArray();
        generator.writeStringField("module_bytes", "");
        generator.writeEndObject();
        generator.writeEndObject();

        generator.writeFieldName("session");
        generator.writeStartObject();
        generator.writeFieldName("Transfer");
        generator.writeStartObject();
        generator.writeFieldName("args");
        generator.writeStartArray();
        generator.writeStartArray();
        generator.writeString("amount");
        generator.writeStartObject();
        generator.writeStringField("bytes", "abc");
        generator.writeStringField("cl_type", "abc");
        generator.writeStringField("parsed", "abc");
        generator.writeEndObject();
        generator.writeEndArray();

        generator.writeStartArray();
        generator.writeString("target");
        generator.writeStartObject();
        generator.writeStringField("bytes", "abc");
        generator.writeFieldName("cl_type");
        generator.writeStartObject();
        generator.writeNumberField("ByteArray", 32);
        generator.writeEndObject();
        generator.writeStringField("parsed", "abc");
        generator.writeEndObject();
        generator.writeEndArray();

        generator.writeStartArray();
        generator.writeString("id");
        generator.writeStartObject();
        generator.writeStringField("bytes", "abc");
        generator.writeFieldName("cl_type");
        generator.writeStartObject();
        generator.writeStringField("Option", "U32");
        generator.writeEndObject();
        generator.writeStringField("parsed", "abc");
        generator.writeEndObject();
        generator.writeEndArray();
        generator.writeEndArray();
        generator.writeEndObject();
        generator.writeEndObject();

        generator.writeStringField("id", "123");

        generator.writeEndObject();
        generator.flush();

        JsonNode pretty = new ObjectMapper().readTree(writer.toString());
        System.out.println(pretty.toPrettyString());

        return new ObjectMapper().readTree(writer.toString()).toPrettyString();

    }

}
