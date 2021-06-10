package com.casper.sdk.json;

import com.casper.sdk.domain.Deploy;
import com.casper.sdk.domain.DeployExecutable;
import com.casper.sdk.domain.DeployHeader;
import com.casper.sdk.domain.Digest;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.LinkedHashSet;

/**
 * FIXME DO NOT USE YES
 */
public class DeployJsonDeserializer extends JsonDeserializer<Deploy> {
    @Override
    public Deploy deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {

        final JsonStreamContext parsingContext = p.getParsingContext();
        final ObjectCodec codec = p.getCodec();
        TreeNode treeNode = codec.readTree(p);

        final TreeNode deployNode = treeNode.get("deploy");

        // If there is a deploy field deserialize its value
        if (deployNode != null) {
            p = deployNode.traverse();
        }
        return deserializeDeploy(p, codec, ctxt);
    }

    private Deploy deserializeDeploy(JsonParser p, final ObjectCodec codec, final DeserializationContext ctxt) throws IOException {

        final TreeNode treeNode = codec.readTree(p);

        return new Deploy(
                readChildNode(treeNode, codec, "hash", Digest.class),
                readChildNode(treeNode, codec, "header", DeployHeader.class),
                readChildNode(treeNode, codec, "payment", DeployExecutable.class),
                readChildNode(treeNode, codec, "session", DeployExecutable.class),
                readChildNode(treeNode, codec, "approvals", LinkedHashSet.class)
        );
    }

    private <T> T readChildNode(final TreeNode digestNode,
                                final ObjectCodec codec,
                                final String name,
                                final Class<T> type) throws IOException {
        final TreeNode child = digestNode.get(name);
        if (child != null) {
            String s = child.toString();
            return new ObjectMapper().readValue(s, type);
            //return  codec.readValue(child.traverse(), type);
        }
        return null;
    }


}
