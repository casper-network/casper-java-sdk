package com.casper.sdk.service.json.deserialize;

import com.casper.sdk.exceptions.ConversionException;
import com.casper.sdk.service.serialization.cltypes.CLValueBuilder;
import com.casper.sdk.types.*;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.NumericNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Deserializer for {@link com.casper.sdk.types.CLValue} type object
 */
public class CLValueJsonDeserializer extends JsonDeserializer<CLValue> {

    private static final String KEY = "key";
    private static final String VALUE = "value";
    private static final String CL_TYPE = "cl_type";
    private static final String BYTES = "bytes";
    private static final String PARSED = "parsed";

    @Override
    public CLValue deserialize(JsonParser p, DeserializationContext context) throws IOException {
        final ObjectCodec codec = p.getCodec();
        return getClValue(codec.readTree(p));
    }

    private CLValue getClValue(final TreeNode treeNode) {
        final TreeNode typeNode = treeNode.get(CL_TYPE);
        final TextNode bytesNode = (TextNode) treeNode.get(BYTES);
        final CLTypeInfo clTypeInfo = getCLTypeInfo(typeNode);
        final Object parsed = getParsed(treeNode.get(PARSED), clTypeInfo);

        if (clTypeInfo instanceof CLMapTypeInfo) {
            return deserializeMap((CLMapTypeInfo) clTypeInfo, bytesNode, parsed);
        } else if (clTypeInfo instanceof CLOptionTypeInfo) {
            return new CLOptionValue(bytesNode.asText(), ((CLOptionTypeInfo) clTypeInfo), parsed);
        } else {
            return new CLValue(bytesNode.asText(), clTypeInfo, parsed);
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
            return new CLOptionTypeInfo(getCLTypeInfo(typeNode.get(CLType.OPTION.getJsonName())));
        } else if (CLType.MAP == clType) {
            return new CLMapTypeInfo(
                    getCLTypeInfo(((ObjectNode) typeNode).findValue(KEY)),
                    getCLTypeInfo(((ObjectNode) typeNode).findValue(VALUE))
            );
        } else {
            return new CLTypeInfo(clType);
        }
    }

    private Object getParsed(final TreeNode treeNode, CLTypeInfo clTypeInfo) {
        if (treeNode instanceof TextNode) {
            return ((TextNode) treeNode).asText();
        } else if (treeNode instanceof NumericNode && CLType.isNumeric(clTypeInfo.getType())) {
            return ((NumericNode) treeNode).bigIntegerValue();
        } else if (CLType.MAP == clTypeInfo.getType()) {
            return treeNode;
        } else {
            return null;
        }
    }

    CLMap deserializeMap(final CLMapTypeInfo typeInfo, final TextNode bytesNode, final Object parsed) {
        return new CLMap(bytesNode.asText(), typeInfo, parseMap(typeInfo, parsed));
    }

    private Map<CLValue, CLValue> parseMap(final CLMapTypeInfo mapTypeInfo, final Object parsed) {
        if (parsed instanceof String) {
            try {
                return parseMap(mapTypeInfo, new ObjectMapper().readTree((String) parsed));
            } catch (JsonProcessingException e) {
                throw new ConversionException(e);
            }
        } else if (parsed instanceof Map) {
            //noinspection unchecked
            return (Map<CLValue, CLValue>) parsed;
        } else if (parsed instanceof ArrayNode) {
            final Map<CLValue, CLValue> map = new LinkedHashMap<>();
            ((ArrayNode) parsed).iterator().forEachRemaining(node -> {
                final JsonNode keyJson =  node.findValue(KEY);
                final JsonNode valueJson = node.findValue(VALUE);
                final CLValue key = CLValueBuilder.buildCLValue(mapTypeInfo.getKeyType().getType(), keyJson.textValue());
                final CLValue value = CLValueBuilder.buildCLValue(mapTypeInfo.getValueType().getType(), valueJson.textValue());
                map.put(key, value);
            });
            return map;
        }
        return null;
    }
}
