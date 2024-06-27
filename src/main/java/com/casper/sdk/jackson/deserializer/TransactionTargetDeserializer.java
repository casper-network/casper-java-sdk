package com.casper.sdk.jackson.deserializer;

import com.casper.sdk.exception.NoSuchTypeException;
import com.casper.sdk.model.deploy.transform.TransformKindV2;
import com.casper.sdk.model.transaction.target.Native;
import com.casper.sdk.model.transaction.target.Session;
import com.casper.sdk.model.transaction.target.Stored;
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
public class TransactionTargetDeserializer extends AbstractAnyOfDeserializer {
    public TransactionTargetDeserializer(JavaType bt, TypeIdResolver idRes, String typePropertyName, boolean typeIdVisible, JavaType defaultImpl) {
        super(bt, idRes, typePropertyName, typeIdVisible, defaultImpl);
    }

    public TransactionTargetDeserializer(AsPropertyTypeDeserializer src, BeanProperty property) {
        super(src, property);
    }

    @Override
    public TypeDeserializer forProperty(final BeanProperty prop) {
        return (prop == _property) ? this : new TransactionTargetDeserializer(this, prop);
    }

    @Override
    protected Class<?> getClassByName(final String classType) throws NoSuchTypeException {
        if (Native.class.getSimpleName().equals(classType)) {
            return Native.class;
        } else if (Session.class.getSimpleName().equals(classType)) {
            return Session.class;
        } else if (Stored.class.getSimpleName().equals(classType)) {
            return Stored.class;
        } else {
            throw new NoSuchTypeException("No such type: " + classType);
        }
    }
}
