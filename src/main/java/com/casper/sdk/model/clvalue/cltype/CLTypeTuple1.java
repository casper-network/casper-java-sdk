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
 * CLType for {@link AbstractCLType#TUPLE1}
 *
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @see AbstractCLType
 * @since 0.0.1
 */
@Getter
@EqualsAndHashCode(callSuper = true, of = {"typeName"})
public class CLTypeTuple1 extends AbstractCLTypeWithChildren {
    private final String typeName = TUPLE1;

    @Override
    @JsonProperty(TUPLE1)
    protected void setChildTypeObjects(final List<Object> childTypeObjects)
            throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
            NoSuchMethodException, SecurityException, NoSuchTypeException {
        super.setChildTypeObjects(childTypeObjects);
    }

    @Override
    @JsonProperty(TUPLE1)
    protected List<Object> getChildTypeObjects() {
        return super.getChildTypeObjects();
    }

    @Override
    public void serializeChildTypes(final SerializerBuffer ser) throws NoSuchTypeException {
        if (!getChildTypes().isEmpty()) {
            getChildTypes().get(0).serialize(ser);
        }
    }

    @Override
    public void deserializeChildTypes(final DeserializerBuffer deser) throws ValueDeserializationException, NoSuchTypeException, DynamicInstanceException {
        // FIXME
    }
}
