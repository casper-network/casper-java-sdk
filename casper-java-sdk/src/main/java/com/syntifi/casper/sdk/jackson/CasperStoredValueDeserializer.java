package com.syntifi.casper.sdk.jackson;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.syntifi.casper.sdk.model.storedvalue.CasperStoredValue;

/**
 * Customize the mapping of Casper's CLValue
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
public class CasperStoredValueDeserializer extends StdDeserializer<CasperStoredValue<? extends Object>> {

    public CasperStoredValueDeserializer() {
        this(null);
    }

    public CasperStoredValueDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public CasperStoredValue<? extends Object> deserialize(JsonParser p, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {

        JsonNode node = p.getCodec().readTree(p);
        var clValueNode = node.get("CLValue");
        String clType = clValueNode.get("cl_type").asText();
        byte[] bytes = clValueNode.get("bytes").asText().getBytes();
        JsonNode toBeParsed = clValueNode.get("parsed");

        switch (clType) {
            case "Bool":
                return new CasperStoredValue<>(clType, bytes, toBeParsed.asBoolean());
            case "I32":
                return new CasperStoredValue<>(clType, bytes, toBeParsed.asInt());
            case "I64":
                return new CasperStoredValue<>(clType, bytes, toBeParsed.asLong());
            case "U8":
                return new CasperStoredValue<>(clType, bytes, toBeParsed.asText().getBytes());
            case "U32":
                return new CasperStoredValue<>(clType, bytes, toBeParsed.asInt());
            case "U64":
                return new CasperStoredValue<>(clType, bytes, toBeParsed.asLong());
            case "U128":
            case "U256":
            case "U512":
            case "String":
                return new CasperStoredValue<>(clType, bytes, toBeParsed.asText());
            default: // Any
                return new CasperStoredValue<>(clType, bytes, (Object) toBeParsed.asText());
        }
    }
}
