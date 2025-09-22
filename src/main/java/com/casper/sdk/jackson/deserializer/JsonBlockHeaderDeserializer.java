package com.casper.sdk.jackson.deserializer;

import com.casper.sdk.model.block.BlockHeader;
import com.casper.sdk.model.block.BlockHeaderV2;
import com.casper.sdk.model.block.JsonBlockHeader;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;

/**
 * @author ian@meywood.com
 */
public class JsonBlockHeaderDeserializer extends JsonDeserializer<BlockHeader> {
    @Override
    public BlockHeader deserialize(final JsonParser p, final DeserializationContext ctx) throws IOException {

        final ObjectNode treeNode = p.readValueAsTree();
        final String fieldName = treeNode.fieldNames().next();
        try (JsonParser parser = treeNode.get(fieldName).traverse()) {
            parser.setCodec(ctx.getParser().getCodec());
            if ("Version1".equals(fieldName)) {
                return parser.readValueAs(JsonBlockHeader.class);
            } else if ("Version2".equals(fieldName)) {
                return parser.readValueAs(BlockHeaderV2.class);
            } else {
                throw new IllegalArgumentException("Unknown BlockHeader type: " + fieldName);
            }
        }
    }
}
