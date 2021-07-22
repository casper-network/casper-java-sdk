package com.casper.sdk.service.json.deserialize;

import com.casper.sdk.types.DeployExecutable;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.util.Iterator;

import static com.casper.sdk.service.json.deserialize.DeserializerContext.peekFieldName;

/**
 * Custom JSON Converts classes that extent {@link DeployExecutable} types from JSON.
 */
public class DeployExecutableJsonDeserializer extends JsonDeserializer<DeployExecutable> {

    /** The factory that does the conversion for specialisations of DeployExecutable */
    private final DeployExecutableFactory factory = new DeployExecutableFactory();

    @Override
    public DeployExecutable deserialize(final JsonParser p, final DeserializationContext context) throws IOException {

        final String fieldName = getFieldName();
        final ObjectCodec codec = p.getCodec();
        final TreeNode treeNode = codec.readTree(p);

        final Iterator<String> iterator = treeNode.fieldNames();
        if (iterator.hasNext()) {
            return factory.create(fieldName, iterator.next(), treeNode, codec);
        }

        throw new IllegalArgumentException("Not a valid DeployExecutable " + p);
    }

    private String getFieldName() {
        return peekFieldName();
    }
}
