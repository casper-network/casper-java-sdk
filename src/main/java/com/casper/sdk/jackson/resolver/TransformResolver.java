package com.casper.sdk.jackson.resolver;

import com.casper.sdk.jackson.deserializer.TransformDeserializer;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.impl.StdTypeResolverBuilder;
import com.casper.sdk.model.clvalue.AbstractCLValue;

import java.util.Collection;

/**
 * Specification of the Custom Type Resolver for CLValue subtype identification. This
 * is used by jackson with the @JsonTypeResolver decorator
 *
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @see AbstractCLValue
 * @since 0.0.1
 */
public class TransformResolver extends StdTypeResolverBuilder {
    @Override
    public TypeDeserializer buildTypeDeserializer(final DeserializationConfig config, final JavaType baseType, final Collection<NamedType> subtypes) {
        return new TransformDeserializer(baseType, null, _typeProperty, _typeIdVisible, baseType);
    }
}