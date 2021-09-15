package com.syntifi.casper.sdk.jackson;

import java.io.IOException;
import java.math.BigInteger;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.syntifi.casper.sdk.exception.NotImplementedException;
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
        JsonNode clValueNode = node.get("CLValue");
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
            case "U32":
                return new CasperStoredValue<>(clType, bytes, toBeParsed.asInt());
            case "U64":
                return new CasperStoredValue<>(clType, bytes, toBeParsed.asLong());
            case "U128":
            case "U256":
            case "U512":
                return new CasperStoredValue<>(clType, bytes, new BigInteger(toBeParsed.asText()));
            case "Unit":
                throw new NotImplementedException();
            case "String":
                return new CasperStoredValue<>(clType, bytes, toBeParsed.asText());
            case "Key":
                throw new NotImplementedException();
            case "URef":
                throw new NotImplementedException();
            case "Optional":
                throw new NotImplementedException();
            case "List":
                throw new NotImplementedException();
            case "ByteArray":
                return new CasperStoredValue<>(clType, bytes, toBeParsed.asText().getBytes());
            case "Result":
                throw new NotImplementedException();
            case "Map":
                throw new NotImplementedException();
            case "Tuple1":
                throw new NotImplementedException();
            case "Tuple2":
                throw new NotImplementedException();
            case "Tuple3":
                throw new NotImplementedException();
            case "PublicKey":
                throw new NotImplementedException();
            default: // Any
                return new CasperStoredValue<>(clType, bytes, (Object) toBeParsed.asText());
        }
    }
}
