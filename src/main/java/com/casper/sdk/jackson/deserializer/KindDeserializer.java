package com.casper.sdk.jackson.deserializer;

import com.casper.sdk.exception.NoSuchTypeException;
import com.casper.sdk.model.deploy.transform.TransformKindV2;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.jsontype.impl.AsPropertyTypeDeserializer;

/**
 * Deserializer for {@link TransformKindV2} types.
 *
 * @author ian@meywood.com
 */
public class KindDeserializer extends AbstractAnyOfDeserializer {
    public KindDeserializer(JavaType bt, TypeIdResolver idRes, String typePropertyName, boolean typeIdVisible, JavaType defaultImpl) {
        super(bt, idRes, typePropertyName, typeIdVisible, defaultImpl);
    }

    public KindDeserializer(AsPropertyTypeDeserializer src, BeanProperty property) {
        super(src, property);
    }

    @Override
    public TypeDeserializer forProperty(final BeanProperty prop) {
        return (prop == _property) ? this : new KindDeserializer(this, prop);
    }

    @Override
    protected Class<?> getClassByName(String classType) throws NoSuchTypeException {
        return TransformKindV2.getClassByName(classType);
    }
}
