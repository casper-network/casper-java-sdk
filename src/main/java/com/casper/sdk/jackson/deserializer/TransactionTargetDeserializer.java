package com.casper.sdk.jackson.deserializer;

import com.casper.sdk.exception.DeserializationException;
import com.casper.sdk.exception.NoSuchTypeException;
import com.casper.sdk.model.transaction.target.*;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
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

    public static final String SESSION = Session.class.getSimpleName();
    public static final String STORED = Stored.class.getSimpleName();
    public static final String RUNTIME = "runtime";
    public static final String MODULE_BYTES = "module_bytes";
    public static final String NATIVE = "\"Native\"";

    @Override
    public TransactionTarget deserialize(final JsonParser p, final DeserializationContext ctxt) throws IOException {
        if (p.getCurrentToken() == JsonToken.START_OBJECT) {
            final ObjectNode treeNode = p.readValueAsTree();
            final Iterator<String> stringIterator = treeNode.fieldNames();
            final String next = stringIterator.next();
            if (SESSION.equals(next)) {
                return createSession(treeNode.get(next));
            } else if (STORED.equals(next)) {
                return createStored(treeNode.get(next), ctxt);
            } else {
                throw new IllegalArgumentException("Unknown transaction target type: " + next);
            }
        } else if (p.getCurrentToken() == JsonToken.VALUE_STRING && NATIVE.equals(p.readValueAsTree().toString())) {
            return new Native();
        } else {
            throw new IllegalArgumentException("Unknown  transaction target type: " + p);
        }
    }

    private Stored createStored(final JsonNode node, final DeserializationContext ctx) throws IOException {
        try {

            return new Stored(
                    createInvocationTarget(node, ctx),
                    TransactionRuntime.getJsonRuntime(node.get(RUNTIME).asText()));
        } catch (NoSuchTypeException e) {
            throw new DeserializationException("Unable to find 'runtime'", e);
        }
    }

    private TransactionInvocationTarget createInvocationTarget(final JsonNode node, final DeserializationContext ctx) throws IOException {
        final JsonNode jsonNode = node.get("id");
        final JsonParser parser = jsonNode.traverse();
        parser.setCodec(ctx.getParser().getCodec());
        return parser.readValueAs(TransactionInvocationTarget.class);
    }

    private Session createSession(final JsonNode node) throws DeserializationException {
        try {
            return new Session(
                    Hex.decode(node.get(MODULE_BYTES).asText()),
                    TransactionRuntime.getJsonRuntime(node.get(RUNTIME).asText())
            );
        } catch (NoSuchTypeException e) {
            throw new DeserializationException("Unable to find required fields", e);
        }
    }
}
