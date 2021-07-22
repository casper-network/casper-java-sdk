package com.casper.sdk.service.json.deserialize;

import com.casper.sdk.types.*;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.node.NumericNode;
import com.fasterxml.jackson.databind.node.TextNode;

import java.io.IOException;

/**
 * Deserializer for {@link com.casper.sdk.types.CLValue} type object
 */
public class CLValueJsonDeserializer extends JsonDeserializer<CLValue> {

    @Override
    public CLValue deserialize(JsonParser p, DeserializationContext context) throws IOException {
        final ObjectCodec codec = p.getCodec();
        return getClValue(codec.readTree(p));
    }

    private CLValue getClValue(final TreeNode treeNode) {
        final TreeNode typeNode = treeNode.get("cl_type");
        final TextNode bytesNode = (TextNode) treeNode.get("bytes");
        final CLTypeInfo clTypeInfo = getCLTypeInfo(typeNode);
        final Object parsed = getParsed(treeNode.get("parsed"), clTypeInfo);

        if (clTypeInfo instanceof CLOptionTypeInfo) {
            return new CLOptionValue(bytesNode.asText(), ((CLOptionTypeInfo) clTypeInfo), parsed);
        } else {
            return new CLValue(bytesNode.asText(), clTypeInfo, parsed);
        }
    }

    private CLTypeInfo getCLTypeInfo(final TreeNode typeNode) {

        final CLType clType = getClType(typeNode);

        if (CLType.BYTE_ARRAY == clType) {
            final TreeNode sizeNode = typeNode.get(CLType.BYTE_ARRAY.getJsonName());
            int size = 0;
            if (sizeNode instanceof NumericNode) {
                size = ((NumericNode) sizeNode).asInt();
            }
            return new CLByteArrayInfo(size);
        } else if (CLType.OPTION == clType) {
            final TreeNode optionNode = typeNode.get(CLType.OPTION.getJsonName());
            final CLTypeInfo interType = getCLTypeInfo(optionNode);
            return new CLOptionTypeInfo(interType);
        } else {
            return new CLTypeInfo(clType);
        }
    }

    private CLType getClType(final TreeNode typeNode) {

        if (typeNode instanceof TextNode) {
            return CLType.fromString(((TextNode) typeNode).asText());
        } else {
            // Complex node where type is the fieldName eg for byte array
            final String typeName = typeNode.fieldNames().next();
            return CLType.fromString(typeName);
        }
    }

    private Object getParsed(final TreeNode treeNode, CLTypeInfo clTypeInfo) {
        if (treeNode instanceof TextNode) {
            return ((TextNode) treeNode).asText();
        } else if (treeNode instanceof NumericNode && CLType.isNumeric(clTypeInfo.getType())) {
            return ((NumericNode) treeNode).bigIntegerValue();
        } else {
            return null;
        }
    }
}
