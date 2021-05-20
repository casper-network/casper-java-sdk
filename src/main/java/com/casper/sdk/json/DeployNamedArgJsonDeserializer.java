package com.casper.sdk.json;

import com.casper.sdk.domain.CLValue;
import com.casper.sdk.domain.DeployNamedArg;
import com.fasterxml.jackson.core.JsonParser;
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
    public DeployNamedArg deserialize(final JsonParser p, final DeserializationContext context) throws IOException {

        final ObjectCodec codec = p.getCodec();
        final TreeNode treeNode = codec.readTree(p);

        return new DeployNamedArg(getName(treeNode.get(0)), getClValue(treeNode.get(1), codec));
    }

    private CLValue getClValue(final TreeNode valueNode, final ObjectCodec codec) throws IOException {
        if (valueNode != null) {

            final JsonParser p = valueNode.traverse();

            // If the code is not set use root codec
            if (p.getCodec() == null) {
                p.setCodec(codec);
            }

            return p.readValueAs(CLValue.class);
        } else {
            return null;
        }
    }


    private String getName(final TreeNode nameMode) {

        if (nameMode instanceof TextNode) {
            return ((TextNode) nameMode).asText();
        }
        // TODO Do we throw? Add validation later
        return "unknown";
    }


}
