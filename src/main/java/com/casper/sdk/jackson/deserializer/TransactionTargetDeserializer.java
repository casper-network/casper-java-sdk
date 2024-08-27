package com.casper.sdk.jackson.deserializer;

import com.casper.sdk.exception.NoSuchTypeException;
import com.casper.sdk.model.transaction.target.*;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.syntifi.crypto.key.encdec.Hex;

import java.io.IOException;
import java.util.Iterator;

/**
 * Deserializer for {@link TransactionTarget} types.
 *
 * @author ian@meywood.com
 */
public class TransactionTargetDeserializer extends JsonDeserializer<TransactionTarget> {

    @Override
    public TransactionTarget deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        if (p.getCurrentToken() == JsonToken.START_OBJECT) {
            final ObjectNode treeNode = p.readValueAsTree();
            final Iterator<String> stringIterator = treeNode.fieldNames();
            final String next = stringIterator.next();
            if (next != null && next.equals(Session.class.getSimpleName())) {
                try {
                    return new Session(Hex.decode(treeNode.get(next).get("module_bytes").asText()),
                            TransactionRuntime.getJsonRuntime(treeNode.get(next).get("runtime").asText()));
                } catch (NoSuchTypeException e) {
                    throw new RuntimeException(e);
                }
            } else {
                throw new IllegalArgumentException("Unknown transaction target type: " + next);
            }
        } else if (p.getCurrentToken() == JsonToken.VALUE_STRING) {
            return new Native();
        } else {
            throw new IllegalArgumentException("Unknown  transaction target type: " + p);
        }
    }

}
