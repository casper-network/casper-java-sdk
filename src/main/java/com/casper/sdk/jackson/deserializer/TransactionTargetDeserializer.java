package com.casper.sdk.jackson.deserializer;

import com.casper.sdk.exception.DeserializationException;
import com.casper.sdk.exception.NoSuchTypeException;
import com.casper.sdk.model.common.Digest;
import com.casper.sdk.model.transaction.target.*;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.syntifi.crypto.key.encdec.Hex;

import java.io.IOException;

import static com.casper.sdk.model.transaction.target.TargetConstants.*;

/**
 * Deserializer for {@link TransactionTarget} types.
 *
 * @author ian@meywood.com
 */
public class TransactionTargetDeserializer extends JsonDeserializer<TransactionTarget> {

    private static final String NATIVE_JSON = "\"Native\"";

    @Override
    public TransactionTarget deserialize(final JsonParser p, final DeserializationContext ctxt) throws IOException {
        if (p.getCurrentToken() == JsonToken.START_OBJECT) {
            final ObjectNode treeNode = p.readValueAsTree();
            final String fieldName = treeNode.fieldNames().next();
            if (SESSION.equals(fieldName)) {
                return createSession(treeNode.get(fieldName));
            } else if (STORED.equals(fieldName)) {
                return createStored(treeNode.get(fieldName), ctxt);
            } else {
                throw new IllegalArgumentException("Unknown transaction target type: " + fieldName);
            }
        } else if (p.getCurrentToken() == JsonToken.VALUE_STRING && NATIVE_JSON.equals(p.readValueAsTree().toString())) {
            return new Native();
        } else {
            throw new IllegalArgumentException("Unknown  transaction target type: " + p.readValueAsTree());
        }
    }

    private Stored createStored(final JsonNode node, final DeserializationContext ctx) throws IOException {
        try {
            return new Stored(
                    createInvocationTarget(node, ctx),
                    TransactionRuntime.fromJson(node.get(RUNTIME).asText()));
        } catch (NoSuchTypeException e) {
            throw new DeserializationException("Unable to find 'runtime'", e);
        }
    }

    private TransactionInvocationTarget createInvocationTarget(final JsonNode node, final DeserializationContext ctx) throws IOException {
        try (final JsonParser parser = node.get(ID).traverse()) {
            parser.setCodec(ctx.getParser().getCodec());
            return parser.readValueAs(TransactionInvocationTarget.class);
        }
    }

    private Session createSession(final JsonNode node) throws DeserializationException {
        try {

            // FIXME use annotation processor for JSON serialization
            return new Session(
                    node.has(IS_INSTALL_UPGRADE) && node.get(IS_INSTALL_UPGRADE).asBoolean(),
                    TransactionRuntime.fromJson(node.get(RUNTIME).asText()),
                    Hex.decode(node.get(MODULE_BYTES).asText()),
                    node.has(TRANSFERRED_VALUE) ? node.get(TRANSFERRED_VALUE).asLong() : 0,
                    node.has(SEED) && !node.get(SEED).isNull() && !"null".equals(node.get(SEED).asText())? new Digest(node.get(SEED).asText()) : null
            );
        } catch (NoSuchTypeException e) {
            throw new DeserializationException("Unable to find required fields", e);
        }
    }
}
