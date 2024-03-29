package com.casper.sdk.model.clvalue.cltype;

import com.casper.sdk.exception.DynamicInstanceException;
import com.casper.sdk.exception.NoSuchTypeException;
import com.fasterxml.jackson.annotation.JsonProperty;
import dev.oak3.sbs4j.DeserializerBuffer;
import dev.oak3.sbs4j.SerializerBuffer;
import dev.oak3.sbs4j.exception.ValueDeserializationException;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * CLType for {@link AbstractCLType#TUPLE3}
 *
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @see AbstractCLType
 * @since 0.0.1
 */
@Getter
@EqualsAndHashCode(callSuper = true, of = {"typeName"})
public class CLTypeTuple3 extends AbstractCLTypeWithChildren {
    private final String typeName = AbstractCLType.TUPLE3;

    @Override
    @JsonProperty(AbstractCLType.TUPLE3)
    protected void setChildTypeObjects(final List<Object> childTypeObjects)
            throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
            NoSuchMethodException, SecurityException, NoSuchTypeException {
        super.setChildTypeObjects(childTypeObjects);
    }

    @Override
    @JsonProperty(AbstractCLType.TUPLE3)
    protected List<Object> getChildTypeObjects() {
        return super.getChildTypeObjects();
    }

    public void serializeChildTypes(SerializerBuffer ser) throws NoSuchTypeException {

        if (getChildTypes().size() >= 3) {
            getChildTypes().get(0).serialize(ser);
            getChildTypes().get(1).serialize(ser);
            getChildTypes().get(2).serialize(ser);
        }
    }

    @Override
    public void deserializeChildTypes(final DeserializerBuffer deser)
            throws ValueDeserializationException, NoSuchTypeException, DynamicInstanceException {
        getChildTypes().add(deserializeChildType(deser));
        getChildTypes().add(deserializeChildType(deser));
        getChildTypes().add(deserializeChildType(deser));
    }
}
