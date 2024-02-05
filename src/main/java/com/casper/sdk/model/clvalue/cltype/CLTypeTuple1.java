package com.casper.sdk.model.clvalue.cltype;

import com.casper.sdk.exception.NoSuchTypeException;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    protected void setChildTypeObjects(List<Object> childTypeObjects)
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
    public boolean isDeserializable() {
        return getChildTypes().stream().anyMatch(AbstractCLType::isDeserializable);
    }
}
