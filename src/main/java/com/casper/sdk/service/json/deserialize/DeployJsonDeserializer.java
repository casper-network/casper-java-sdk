package com.casper.sdk.service.json.deserialize;

import com.casper.sdk.types.*;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.util.Set;

import static com.casper.sdk.service.json.deserialize.DeserializerContext.pushFieldName;

/**
 * The JSON Deserializer for a {@link Deploy}.
 */
public class DeployJsonDeserializer extends JsonDeserializer<Deploy> {

    @Override
    public Deploy deserialize(final JsonParser p, final DeserializationContext ctxt) throws IOException {

        final ObjectCodec codec = p.getCodec();
        TreeNode treeNode = codec.readTree(p);

        final TreeNode deployNode = treeNode.get("deploy");

        // If there is a deploy field deserialize its value
        if (deployNode != null) {
            treeNode = codec.readTree(deployNode.traverse());
        }
        return deserializeDeploy(treeNode, codec);
    }

    private Deploy deserializeDeploy(final TreeNode treeNode,
                                     final ObjectCodec codec) throws IOException {

        //noinspection Convert2Diamond
        return new Deploy(
                readChildNode(treeNode, codec, "hash", Digest.class),
                readChildNode(treeNode, codec, "header", DeployHeader.class),
                readChildNode(treeNode, codec, "payment", DeployExecutable.class),
                readChildNode(treeNode, codec, "session", DeployExecutable.class),
                readChildNode(treeNode, codec, "approvals", new TypeReference<Set<DeployApproval>>() {})
        );
    }

    private <T> T readChildNode(final TreeNode digestNode,
                                final ObjectCodec codec,
                                final String name,
                                final Class<T> type) throws IOException {

        try {
            pushFieldName(name);
            final TreeNode child = digestNode.get(name);
            if (child != null) {
                final JsonParser childParser = child.traverse();
                // Ensure the code is passed on the child parser
                if (childParser.getCodec() == null) {
                    childParser.setCodec(codec);
                }

                return codec.readValue(childParser, type);
            }
        } finally {
            DeserializerContext.popFieldName();
        }
        return null;
    }

    private <T> T readChildNode(final TreeNode digestNode,
                                final ObjectCodec codec,
                                final String name,
                                final TypeReference<T> type) throws IOException {

        try {
            pushFieldName(name);
            final TreeNode child = digestNode.get(name);
            if (child != null) {
                final JsonParser childParser = child.traverse();
                // Ensure the code is passed on the child parser
                if (childParser.getCodec() == null) {
                    childParser.setCodec(codec);
                }

                return codec.readValue(childParser, type);
            }
        } finally {
            DeserializerContext.popFieldName();
        }
        return null;
    }
}
