package com.casper.sdk.model.clvalue.cltype;

import com.casper.sdk.exception.NoSuchTypeException;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    protected void setChildTypeObjects(List<Object> childTypeObjects)
            throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
            NoSuchMethodException, SecurityException, NoSuchTypeException {
        super.setChildTypeObjects(childTypeObjects);
    }

    @Override
    @JsonProperty(AbstractCLType.TUPLE3)
    protected List<Object> getChildTypeObjects() {
        return super.getChildTypeObjects();
    }
}
