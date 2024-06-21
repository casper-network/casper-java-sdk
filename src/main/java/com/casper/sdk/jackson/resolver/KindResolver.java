package com.casper.sdk.jackson.resolver;

import com.casper.sdk.jackson.deserializer.KindDeserializer;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.impl.StdTypeResolverBuilder;

import java.util.Collection;

/**
 * Custom resolver for Kind subtypes.
 *
 * @author ian@meywood.com
 */
public class KindResolver extends StdTypeResolverBuilder {
    @Override
    public TypeDeserializer buildTypeDeserializer(final DeserializationConfig config,
                                                  final JavaType baseType,
                                                  final Collection<NamedType> subtypes) {
        return new KindDeserializer(baseType, null, _typeProperty, _typeIdVisible, baseType);
    }
}
