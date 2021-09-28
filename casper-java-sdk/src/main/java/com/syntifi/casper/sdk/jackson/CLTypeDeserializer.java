package com.syntifi.casper.sdk.jackson;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.jsontype.impl.AsPropertyTypeDeserializer;
import com.fasterxml.jackson.databind.node.TreeTraversingParser;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.syntifi.casper.sdk.exception.NoSuchTypeException;
import com.syntifi.casper.sdk.model.storedvalue.clvalue.CLTypeData;

/**
 * Core Deserializer for the CLValue property. This deserializer is used by the {@link CLTypeResolver} 
 * to return the correct CLType object in Java depending on the cl_type sent over json
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 * @see AbstractCLValue
 */
public class CLTypeDeserializer extends AsPropertyTypeDeserializer {

    public CLTypeDeserializer(final JavaType bt, final TypeIdResolver idRes, final String typePropertyName,
            final boolean typeIdVisible, JavaType defaultImpl) {
        super(bt, idRes, typePropertyName, typeIdVisible, defaultImpl);
    }

    public CLTypeDeserializer(final AsPropertyTypeDeserializer src, final BeanProperty property) {
        super(src, property);
    }

    @Override
    public TypeDeserializer forProperty(final BeanProperty prop) {
        return (prop == _property) ? this : new CLTypeDeserializer(this, prop);
    }

    @Override
    public Object deserializeTypedFromObject(final JsonParser jp, final DeserializationContext ctxt)
            throws IOException {
        JsonNode node = jp.readValueAsTree();
        Class<?> subType;
        try {
            subType = findSubType(node);
        } catch (NoSuchTypeException e) {
            throw new IOException("Parse error", e);
        }
        TypeFactory factory = new ObjectMapper().getTypeFactory();
        JavaType type = factory.constructType(subType);

        try (JsonParser jsonParser = new TreeTraversingParser(node, jp.getCodec())) {
            if (jsonParser.getCurrentToken() == null) {
                jsonParser.nextToken();
            }
            JsonDeserializer<Object> deser = ctxt.findContextualValueDeserializer(type, _property);
            return deser.deserialize(jsonParser, ctxt);
        }
    }

    protected Class<?> findSubType(JsonNode node) throws IOException, NoSuchTypeException {
        Class<?> subType;
        JsonNode clType = node.get("cl_type");

        if (clType.isObject()) {
            Map.Entry<String, JsonNode> parentCLType = clType.fields().next();
            subType = CLTypeData.getClassByName(parentCLType.getKey());
        } else {
            String type = clType.asText();
            subType = CLTypeData.getClassByName(type);
        }
        return subType;
    }
}
