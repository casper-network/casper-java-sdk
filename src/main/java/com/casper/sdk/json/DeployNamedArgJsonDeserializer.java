package com.casper.sdk.json;

import com.casper.sdk.domain.CLType;
import com.casper.sdk.domain.CLValue;
import com.casper.sdk.domain.DeployNamedArg;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.node.TextNode;

import java.io.IOException;

/**
 * Converts a DeployNamedArg from JSON
 * <p/>
 * Strangely the arg is held within a single array element eg:
 * <pre>
 * [
 *  "amount",
 *     {
 *       "cl_type": "U512",
 *       "bytes": "05005550b405",
 *       "parsed": "24500000000"
 *      }
 *  ]</pre>
 */
public class DeployNamedArgJsonDeserializer extends JsonDeserializer<DeployNamedArg> {

    @Override
    public DeployNamedArg deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {

        ObjectCodec codec = p.getCodec();
        TreeNode treeNode = codec.readTree(p);

        return new DeployNamedArg(getName(treeNode), getClValue(treeNode));
    }

    private CLValue getClValue(TreeNode treeNode) {

        final TreeNode valueNode = treeNode.get(1);
        final TextNode typeNode = (TextNode) valueNode.get("cl_type");
        final TextNode bytesNode = (TextNode) valueNode.get("bytes");

        return new CLValue(bytesNode.asText(), CLType.fromString(typeNode.asText()));
    }

    private String getName(TreeNode treeNode) {
        TreeNode arg = treeNode.get(0);
        if (arg instanceof TextNode) {
            return  ((TextNode) arg).asText();
        }
        // TODO Do we throw? Add validation later
        return "unknown";
    }
}
