package com.syntifi.casper.sdk.model.clvalue.cltype;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.syntifi.casper.sdk.exception.NoSuchTypeException;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * Base class for all types which have an array of child types
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
@Setter
@Getter
@EqualsAndHashCode(callSuper = false, of = { "childTypes" })
public abstract class AbstractCLTypeWithChildren extends AbstractCLType {

    @JsonIgnore
    private List<AbstractCLType> childTypes = new ArrayList<>();

    private List<Object> childTypeObjects;

    protected void setChildTypeObjects(List<Object> childTypeObjects)
            throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
            NoSuchMethodException, SecurityException, NoSuchTypeException {
        this.childTypeObjects = childTypeObjects;
        this.loadCLTypes(childTypeObjects);
    }

    protected List<Object> getChildTypeObjects() {
        if (this.childTypeObjects == null) {
            this.childTypeObjects = new ArrayList<>();
        }
        this.childTypeObjects.clear();

        for (AbstractCLType childType : getChildTypes()) {
            if (childType instanceof AbstractCLTypeBasic) {
                this.childTypeObjects.add(childType.getTypeName());
            } else {
                this.childTypeObjects.add(childType);
            }
        }

        return this.childTypeObjects;
    }

    @JsonIgnore
    public CLTypeData getChildClTypeData(int index) throws NoSuchTypeException {
        return CLTypeData.getTypeByName(getChildTypes().get(index).getTypeName());
    }

    protected void loadCLTypes(List<Object> childTypeObjects)
            throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
            NoSuchMethodException, SecurityException, NoSuchTypeException {
        childTypes.clear();

        if (childTypeObjects != null) {
            for (Object childTypeObject : childTypeObjects) {
                addChildType(childTypeObject, childTypes);
            }
        }
    }

    private void addChildType(Object childTypeObject, List<AbstractCLType> parent)
            throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
            NoSuchMethodException, SecurityException, NoSuchTypeException {
        if (childTypeObject instanceof String) {
            parent.add(CLTypeData.getTypeByName(childTypeObject.toString()).getClTypeClass().getConstructor()
                    .newInstance());
        } else if (childTypeObject instanceof ArrayList) {
            for (Object child : (ArrayList<?>) childTypeObject) {
                addChildType(child, parent);
            }
        } else if (childTypeObject instanceof LinkedHashMap) {
            LinkedHashMap<?, ?> subChildTypes = (LinkedHashMap<?, ?>) childTypeObject;

            for (Entry<?, ?> entry : subChildTypes.entrySet()) {
                AbstractCLType nextParent = CLTypeData.getTypeByName(entry.getKey().toString()).getClTypeClass()
                        .getConstructor().newInstance();
                parent.add(nextParent);
                addChildType(entry.getValue(), ((AbstractCLTypeWithChildren) nextParent).getChildTypes());
            }
        }
    }
}
