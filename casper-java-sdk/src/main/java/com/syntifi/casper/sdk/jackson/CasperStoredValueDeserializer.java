package com.syntifi.casper.sdk.jackson;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
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
            throws IOException {

        JsonNode node = p.getCodec().readTree(p);
        var clValueNode = node.get("CLValue");
        String clType = clValueNode.get("cl_type").asText();
        byte[] bytes = clValueNode.get("bytes").asText().getBytes();
        JsonNode toBeParsed = clValueNode.get("parsed");

        return CasperStoredValue.fromValue(clType, bytes, toBeParsed);
    }
}
