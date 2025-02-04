package com.casper.sdk.jackson.deserializer;

import com.casper.sdk.model.clvalue.cltype.AbstractCLType;
import com.casper.sdk.model.contract.entrypoint.EntryPointAccess;
import com.casper.sdk.model.contract.entrypoint.EntryPointArg;
import com.casper.sdk.model.contract.entrypoint.EntryPointType;
import com.casper.sdk.model.contract.entrypoint.EntryPointV1;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.List;

/**
 * Custom deserializer for {@link EntryPointV1} objects
 *
 * @author ian@meywood.com
 */
public class EntryPointV1Deserializer extends JsonDeserializer<EntryPointV1> {
    @Override
    public EntryPointV1 deserialize(final JsonParser p, final DeserializationContext ctxt) throws IOException {

        final JsonNode mapNode = p.getCodec().readTree(p);
        final JsonNode entryPointNode = mapNode.get("entry_point");

        return EntryPointV1.builder()
                .name(mapNode.get("name").asText())
                .access(getNestedObject(entryPointNode, ctxt, "access", EntryPointAccess.class))
                .ret(getNestedObject(entryPointNode, ctxt, "ret", AbstractCLType.class))
                .entryPointType(getNestedObject(entryPointNode, ctxt, "entry_point_type", EntryPointType.class))
                .args(getNestedObject(entryPointNode, ctxt, "args", new TypeReference<List<EntryPointArg>>() {
                }))
                .build();
    }

    private <T> T getNestedObject(final JsonNode node,
                                  final DeserializationContext ctx,
                                  final String name,
                                  final Class<T> type) throws IOException {
        if (node != null) {
            try (final JsonParser parser = node.get(name).traverse()) {
                parser.setCodec(ctx.getParser().getCodec());
                return parser.readValueAs(type);
            }
        } else {
            return null;
        }
    }

    private <T> T getNestedObject(final JsonNode node,
                                  final DeserializationContext ctx,
                                  final String name,
                                  final TypeReference<T> type) throws IOException {
        if (node != null) {
            try (final JsonParser parser = node.get(name).traverse()) {
                parser.setCodec(ctx.getParser().getCodec());
                return parser.readValueAs(type);
            }
        } else {
            return null;
        }
    }

}

