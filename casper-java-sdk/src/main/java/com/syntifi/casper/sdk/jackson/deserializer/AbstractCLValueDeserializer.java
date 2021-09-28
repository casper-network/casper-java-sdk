package com.syntifi.casper.sdk.jackson.deserializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.syntifi.casper.sdk.exception.CLValueDecodeException;
import com.syntifi.casper.sdk.exception.DeserializationException;
import com.syntifi.casper.sdk.exception.DynamicInstanceException;
import com.syntifi.casper.sdk.exception.NoSuchTypeException;
import com.syntifi.casper.sdk.model.storedvalue.clvalue.AbstractCLValue;
import com.syntifi.casper.sdk.model.storedvalue.clvalue.CLType;
import com.syntifi.casper.sdk.model.storedvalue.clvalue.CLTypeData;
import com.syntifi.casper.sdk.model.storedvalue.clvalue.encdec.CLValueDecoder;

/**
 * Customize the deserialing mapping of Casper's CLValue properties
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
public abstract class AbstractCLValueDeserializer<T extends AbstractCLValue<?>> extends JsonDeserializer<T> {
    private static final String CL_TYPE_JSON_PROP_NAME = "cl_type";

    @Override
    public T deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        T object = this.getInstanceOf();

        JsonNode node = p.getCodec().readTree(p);

        try {
            object.setClType(extractCLTypeData(new CLType(), node.get(CL_TYPE_JSON_PROP_NAME)));
        } catch (NoSuchTypeException e) {
            throw new DeserializationException("Error deserializing CLValues", e);
        }

        object.setBytes(node.get("bytes").asText());

        if (node.has("parsed")) {
            object.setParsed(node.get("parsed").asText());
        }

        // Starts the decoding cascade
        try (CLValueDecoder clvd = new CLValueDecoder(object.getBytes())) {
            object.decode(clvd);
        } catch (CLValueDecodeException | DynamicInstanceException e) {
            throw new DeserializationException("Error deserializing CLValues", e);
        }

        return object;
    }

    /**
     * Extracts {@link CLType} from json's cl_type information, keeping SDK
     * consistent
     * 
     * @param clType object that holds the current level of type data
     * @param node   cl_type node from jackson
     * @return the constructed {@link CLType}
     * @throws NoSuchTypeException unknown CLType found
     */
    private CLType extractCLTypeData(CLType clType, JsonNode node) throws NoSuchTypeException {
        if (node.isObject()) {
            ObjectNode clTypeNode = (ObjectNode) node;
            Iterator<Entry<String, JsonNode>> fieldsIterator = clTypeNode.fields();

            while (fieldsIterator.hasNext()) {
                Entry<String, JsonNode> item = fieldsIterator.next();
                clType.setClTypeData(CLTypeData.getTypeByName(item.getKey()));
                clType.setChildTypes(new ArrayList<>());
                JsonNode subItem = item.getValue();

                if (subItem.isObject()) {
                    // In case of a Map cltype, the subItem is of type {"key": CLType, "value":
                    // CLType} ,
                    // i.e. the key of the Jsonobject does not represent a CLType, but the value
                    // does
                    // Same for Result - "{"ok": CLType, "err": CLType}"
                    if (subItem.has("key") && subItem.has("value") && subItem.size() == 2) {
                        extractCLTypeDataMap(clType, subItem);
                    } else if (subItem.has("ok") && subItem.has("err") && subItem.size() == 2) {
                        extractCLTypeDataResult(clType, subItem);
                    } else {
                        clType.getChildTypes().add(extractCLTypeData(new CLType(), subItem));
                    }
                } else if (subItem.isArray()) {
                    extractCLTypeDataArray(clType, item);
                } else if (item.getKey().equals(CLType.BYTE_ARRAY)) {
                    clType.setChildTypes(null);
                } else {
                    CLType child = new CLType();
                    child.setClTypeData(CLTypeData.getTypeByName(subItem.asText()));
                    clType.getChildTypes().add(child);
                }
            }
        } else {
            clType.setClTypeData(CLTypeData.getTypeByName(node.asText()));
        }

        return clType;
    }

    private void extractCLTypeDataArray(CLType clType, Entry<String, JsonNode> item) throws NoSuchTypeException {
        ArrayNode clSubTypes = (ArrayNode) item.getValue();
        Iterator<JsonNode> subTypesIterator = clSubTypes.elements();

        while (subTypesIterator.hasNext()) {
            JsonNode subType = subTypesIterator.next();
            clType.getChildTypes().add(extractCLTypeData(new CLType(), subType));
        }
    }

    private void extractCLTypeDataMap(CLType clType, JsonNode subItem) throws NoSuchTypeException {
        List<String> labels = Arrays.asList("key", "value");
        for (String string : labels) {
            CLType keyVal = new CLType();
            keyVal.setClTypeData(CLTypeData.getTypeByName(subItem.get(string).asText()));
            clType.getChildTypes().add(keyVal);
        }
    }

    private void extractCLTypeDataResult(CLType clType, JsonNode subItem) throws NoSuchTypeException {
        List<String> labels = Arrays.asList("ok", "err");
        for (String string : labels) {
            CLType keyVal = new CLType();
            keyVal.setClTypeData(CLTypeData.getTypeByName(subItem.get(string).asText()));
            clType.getChildTypes().add(keyVal);
        }
    }

    /**
     * Returns the implementation of root {@link AbstractCLValue} class
     * 
     * @return the instance of a {@link AbstractCLValue} implementation
     */
    protected abstract T getInstanceOf();
}
