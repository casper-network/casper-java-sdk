package com.casper.sdk.jackson.resolver;

import com.casper.sdk.jackson.deserializer.CLTypeDeserializer;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.impl.StdTypeResolverBuilder;
import com.casper.sdk.model.clvalue.cltype.AbstractCLType;

import java.util.Collection;

/**
 * Specification of the Custom Type Resolver for CLType subtype identification.
 * This is used by jackson with the @JsonTypeResolver decorator
 *
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @see AbstractCLType
 * @since 0.0.1
 */
public class CLTypeResolver extends StdTypeResolverBuilder {
    @Override
    public TypeDeserializer buildTypeDeserializer(final DeserializationConfig config, final JavaType baseType,
                                                  final Collection<NamedType> subtypes) {
        return new CLTypeDeserializer(baseType, null, _typeProperty, _typeIdVisible, baseType);
    }
}