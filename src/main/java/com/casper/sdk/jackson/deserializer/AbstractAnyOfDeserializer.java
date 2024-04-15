package com.casper.sdk.jackson.deserializer;

import com.casper.sdk.exception.NoSuchTypeException;
import com.casper.sdk.jackson.resolver.CLValueResolver;
import com.casper.sdk.model.clvalue.AbstractCLValue;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.jsontype.impl.AsPropertyTypeDeserializer;
import com.fasterxml.jackson.databind.node.TreeTraversingParser;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.io.IOException;

/**
 * Core Deserializer for the CLValue property. This deserializer is used by the
 * {@link CLValueResolver} to return the correct CLType object in Java depending
 * on the cl_type sent over json
 *
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @see AbstractCLValue
 * @since 0.0.1
 */
public abstract class AbstractAnyOfDeserializer extends AsPropertyTypeDeserializer {

    protected AbstractAnyOfDeserializer(final JavaType bt, final TypeIdResolver idRes, final String typePropertyName,
                                        final boolean typeIdVisible, JavaType defaultImpl) {
        super(bt, idRes, typePropertyName, typeIdVisible, defaultImpl);
    }

    protected AbstractAnyOfDeserializer(final AsPropertyTypeDeserializer src, final BeanProperty property) {
        super(src, property);
    }

    @Override
    public Object deserializeTypedFromObject(final JsonParser jp, final DeserializationContext ctxt)
            throws IOException {
        JsonNode node = jp.readValueAsTree();
        Class<?> subType;
        JsonNode subTypeNode = getTypeNode(node);
        try {
            String anyOfType = subTypeNode.isObject() ? subTypeNode.fieldNames().next() : subTypeNode.asText();
            subType = getClassByName(anyOfType);
        } catch (NoSuchTypeException e) {
            throw new IOException("Parse error", e);
        }
        TypeFactory factory = ctxt.getTypeFactory();
        JavaType type = factory.constructType(subType);

        try (JsonParser jsonParser = new TreeTraversingParser(node, jp.getCodec())) {
            if (jsonParser.getCurrentToken() == null) {
                jsonParser.nextToken();
            }
            JsonDeserializer<Object> deser = ctxt.findContextualValueDeserializer(type, _property);
            return deser.deserialize(jsonParser, ctxt);
        }
    }

    /**
     * Returns the node which contains the type key.
     * <p>
     * Override if you have a child node which holds the type information.
     *
     * @param currentNode the current deserialization node
     * @return node which contains the type key.
     */
    protected JsonNode getTypeNode(JsonNode currentNode) {
        return currentNode;
    }

    /**
     * Method that returns the instance of the found type
     *
     * @param classType the name of the class type
     * @return {@link Class} of the type
     * @throws NoSuchTypeException thrown if no type for the given classType String
     */
    protected abstract Class<?> getClassByName(String classType) throws NoSuchTypeException;
}
