package com.casper.sdk.jackson.deserializer;

import com.casper.sdk.exception.CasperClientException;
import com.casper.sdk.model.transaction.entrypoint.CustomEntryPoint;
import com.casper.sdk.model.transaction.entrypoint.TransactionEntryPoint;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.util.Iterator;

/**
 * The Deserializer for {@link TransactionEntryPoint} objects as Jackson in not able match nested Rust enum types
 *
 * @author ian@meywood.com
 */
public class TransactionEntryPointDeserializer extends JsonDeserializer<TransactionEntryPoint> {

    @Override
    public TransactionEntryPoint deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {

        if (p.getCurrentToken() == JsonToken.START_OBJECT) {
            // Nested rust enums are represented as objects in JSON
            return createNestedEntryPoint(p.readValueAsTree());
        } else if (p.getCurrentToken() == JsonToken.VALUE_STRING) {
            // Standard Rust enum types are read from string
            return createSimpleEntryPoint(p.getValueAsString());
        } else {
            throw new IllegalArgumentException("Unknown scheduling type: " + p);
        }
    }

    private CustomEntryPoint createNestedEntryPoint(final ObjectNode objectNode) {
        final Iterator<String> stringIterator = objectNode.fieldNames();
        final String next = stringIterator.next();
        return new CustomEntryPoint(objectNode.get(next).asText());
    }

    private TransactionEntryPoint createSimpleEntryPoint(final String name) {
        try {
            //noinspection unchecked
            final Class<TransactionEntryPoint> clazz = (Class<TransactionEntryPoint>) Class.forName(buildClassName(name));
            return clazz.newInstance();
        } catch (Exception e) {
            throw new CasperClientException("Invalid TransactionEntryPoint " + name, e);
        }
    }

    private String buildClassName(String name) {
        return TransactionEntryPoint.class.getPackage().getName() + "." + name + "EntryPoint";
    }
}
