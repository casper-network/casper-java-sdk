package com.casper.sdk.json.deserialize;

import com.casper.sdk.domain.DeployExecutable;
import com.casper.sdk.domain.ModuleBytes;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonStreamContext;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import static com.casper.sdk.json.deserialize.DeserializerContext.peekFieldName;

/**
 * Custom JSON Converts classes that extent {@link DeployExecutable} types from JSON.
 */
public class DeployExecutableJsonDeserializer extends JsonDeserializer<DeployExecutable> {

    /** The factory that does the conversion for specialisations of DeployExecutable */
    private final DeployExecutableFactory factory = new DeployExecutableFactory();

    @Override
    public DeployExecutable deserialize(final JsonParser p, final DeserializationContext context) throws IOException {

        final JsonStreamContext parsingContext = p.getParsingContext();
        final String fieldName = getFieldName(parsingContext);
        final ObjectCodec codec = p.getCodec();
        final TreeNode treeNode = codec.readTree(p);

        final Iterator<String> iterator = treeNode.fieldNames();
        if (iterator.hasNext()) {
            return factory.create(fieldName, iterator.next(), treeNode, codec);
        }

        // TODO should we throw here?
        return new ModuleBytes(new ArrayList<>());
    }

    private String getFieldName(JsonStreamContext parsingContext) {
        return peekFieldName();
    }
}
