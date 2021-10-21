package com.syntifi.casper.sdk.model.clvalue.cltype;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.syntifi.casper.sdk.exception.NoSuchTypeException;

import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * CLType for {@link AbstractCLType.TUPLE2}
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @see AbstractCLType
 * @since 0.0.1
 */
@Getter
@EqualsAndHashCode(callSuper = true, of = { "typeName" })
public class CLTypeTuple2 extends AbstractCLTypeWithChildren {
    private final String typeName = AbstractCLType.TUPLE2;

    @Override
    @JsonProperty(AbstractCLType.TUPLE2)
    protected void setChildTypeObjects(List<Object> childTypeObjects)
            throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
            NoSuchMethodException, SecurityException, NoSuchTypeException {
        super.setChildTypeObjects(childTypeObjects);
    }

    @Override
    @JsonProperty(AbstractCLType.TUPLE2)
    protected List<Object> getChildTypeObjects() {
        return super.getChildTypeObjects();
    }
}
