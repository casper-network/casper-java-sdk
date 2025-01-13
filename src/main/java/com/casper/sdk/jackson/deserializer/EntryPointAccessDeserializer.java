package com.casper.sdk.jackson.deserializer;

import com.casper.sdk.model.contract.entrypoint.EntryPointAccess;
import com.casper.sdk.model.contract.entrypoint.GroupsAccess;
import com.casper.sdk.model.contract.entrypoint.PublicAccess;
import com.casper.sdk.model.contract.entrypoint.TemplateAccess;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Custom deserializer for {@link EntryPointAccess}
 * @author ian@meywood.com
 */
public class EntryPointAccessDeserializer extends JsonDeserializer<EntryPointAccess> {

    @Override
    public EntryPointAccess deserialize(final JsonParser p, final DeserializationContext ctxt) throws IOException, JacksonException {

        if (p.getCurrentToken() == JsonToken.START_OBJECT) {
            // Nested rust enums are represented as objects in JSON
            return createNestedEntryPointAccess(p.readValueAsTree());
        } else if (p.getCurrentToken() == JsonToken.VALUE_STRING) {
            // Standard Rust enum types are read from string
            return createSimpleEntryPointAccess(p.getValueAsString());
        } else {
            throw new IllegalArgumentException("Unknown scheduling type: " + p);
        }
    }

    private EntryPointAccess createNestedEntryPointAccess(final ObjectNode treeNode) {
        final ArrayNode groups = (ArrayNode) treeNode.get("Groups");
        final List<String> groupNames = new ArrayList<>();
        if (groups != null) {
            groups.forEach(jsonNode -> groupNames.add(jsonNode.asText()));
        }
        return new GroupsAccess(groupNames);
    }

    private EntryPointAccess createSimpleEntryPointAccess(final String valueAsString) {
        if ("Public".equals(valueAsString)) {
            return new PublicAccess();
        } else if ("Template".equals(valueAsString)) {
             return new TemplateAccess();
        } else {
            throw new IllegalArgumentException("Unknown entry point access type: " + valueAsString);
        }
    }
}
