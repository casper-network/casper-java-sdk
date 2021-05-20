package com.casper.sdk.json;

import com.casper.sdk.domain.DeployExecutable;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Converts all DeployExecutable types from JSON.
 */
public class DeployExecutableJsonDeserializer extends JsonDeserializer<DeployExecutable> {

    /** The factory that does the conversion for specialisations of DeployExecutable */
    private final DeployExecutableFactory factory = new DeployExecutableFactory();

    @Override
    public DeployExecutable deserialize(final JsonParser p, final DeserializationContext context) throws IOException {

        final ObjectCodec codec = p.getCodec();
        final TreeNode treeNode = codec.readTree(p);
        final Iterator<String> iterator = treeNode.fieldNames();
        if (iterator.hasNext()) {
            return factory.create(iterator.next(), treeNode, codec);
        }
        // TODO We should we throw here
        return new DeployExecutable(new ArrayList<>());
    }
}
