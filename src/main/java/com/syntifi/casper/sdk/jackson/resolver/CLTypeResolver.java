package com.syntifi.casper.sdk.jackson.resolver;

import java.util.Collection;

import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.impl.StdTypeResolverBuilder;
import com.syntifi.casper.sdk.jackson.deserializer.CLTypeDeserializer;
import com.syntifi.casper.sdk.model.clvalue.cltype.AbstractCLType;

/**
 * Specification of the Custom Type Resolver for CLType subtype identification.
 * This is used by jackson with the @JsonTypeResolver decorator
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 * @see AbstractCLType
 */
public class CLTypeResolver extends StdTypeResolverBuilder {
    @Override
    public TypeDeserializer buildTypeDeserializer(final DeserializationConfig config, final JavaType baseType,
            final Collection<NamedType> subtypes) {
        return new CLTypeDeserializer(baseType, null, _typeProperty, _typeIdVisible, baseType);
    }
}