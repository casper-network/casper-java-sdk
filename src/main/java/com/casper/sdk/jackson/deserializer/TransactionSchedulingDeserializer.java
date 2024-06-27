package com.casper.sdk.jackson.deserializer;

import com.casper.sdk.model.transaction.scheduling.FutureEra;
import com.casper.sdk.model.transaction.scheduling.FutureTimestamp;
import com.casper.sdk.model.transaction.scheduling.Standard;
import com.casper.sdk.model.transaction.scheduling.TransactionScheduling;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Iterator;

/**
 * Custom deserializer for {@link TransactionScheduling} objects as Jackson in not able match nested Rust enum types
 * using annotations.
 *
 * @author ian@meywood.com
 */
public class TransactionSchedulingDeserializer extends JsonDeserializer<TransactionScheduling> {
    @Override
    public TransactionScheduling deserialize(final JsonParser p, final DeserializationContext ctxt) throws IOException {

        // Nested rust enums are represented as objects in JSON
        if (p.getCurrentToken() == JsonToken.START_OBJECT) {
            final ObjectNode treeNode = p.readValueAsTree();
            final Iterator<String> stringIterator = treeNode.fieldNames();
            final String next = stringIterator.next();
            if (next.equals(FutureTimestamp.class.getSimpleName())) {
                return new FutureTimestamp(treeNode.get(next).asText());
            } else if (next.equals(FutureEra.class.getSimpleName())) {
                return new FutureEra(new BigInteger(treeNode.get(next).asText()));
            } else {
                throw new IllegalArgumentException("Unknown scheduling type: " + next);
            }
        } else if (p.getCurrentToken() == JsonToken.VALUE_STRING) {
            // Normal enum
            return new Standard();
        } else {
            throw new IllegalArgumentException("Unknown scheduling type: " + p);
        }
    }
}

