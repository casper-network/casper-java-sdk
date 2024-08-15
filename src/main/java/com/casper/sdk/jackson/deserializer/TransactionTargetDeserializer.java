package com.casper.sdk.jackson.deserializer;

import com.casper.sdk.model.transaction.target.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.lang.reflect.Field;
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
            if (next.equals(Session.class.getSimpleName())) {
                return new Session("Session", treeNode.get(next).get("module_bytes").asText().getBytes(),
                        getTransactionRuntime(treeNode.get(next).get("runtime").asText()));
            } else {
                throw new IllegalArgumentException("Unknown transaction target type: " + next);
            }
        } else if (p.getCurrentToken() == JsonToken.VALUE_STRING) {
            return new Native();
        } else {
            throw new IllegalArgumentException("Unknown  transaction target type: " + p);
        }
    }

    private TransactionRuntime getTransactionRuntime(final String runtime){

        for (Field field : TransactionRuntime.class.getFields()){
            if (runtime.equals(field.getAnnotation(JsonProperty.class).value())){
                return TransactionRuntime.valueOf(field.getName());
            }
        }

        throw new IllegalArgumentException("Unknown transaction runtime type: " + runtime);

    }


}
