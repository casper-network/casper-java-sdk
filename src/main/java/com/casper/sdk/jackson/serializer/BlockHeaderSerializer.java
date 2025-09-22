package com.casper.sdk.jackson.serializer;

import com.casper.sdk.model.block.BlockHeader;
import com.casper.sdk.model.block.BlockHeaderV2;
import com.casper.sdk.model.block.JsonBlockHeader;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * @author ian@meywood.com
 */
public class BlockHeaderSerializer extends JsonSerializer<BlockHeader> {

    @Override
    public void serialize(final BlockHeader blockHeader,
                          final JsonGenerator jsonGenerator,
                          final SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();

        if (blockHeader instanceof JsonBlockHeader) {
            jsonGenerator.writeFieldName("Version1");
        } else if (blockHeader instanceof BlockHeaderV2) {
            jsonGenerator.writeFieldName("Version2");

        } else {
            throw new IllegalArgumentException("Unsupported block header: " + blockHeader);
        }
        jsonGenerator.writeObject(blockHeader);
        jsonGenerator.writeEndObject();
    }
}
