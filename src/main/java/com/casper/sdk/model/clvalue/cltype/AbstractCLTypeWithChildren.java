package com.casper.sdk.model.clvalue.cltype;

import com.casper.sdk.exception.DynamicInstanceException;
import com.casper.sdk.exception.NoSuchTypeException;
import com.fasterxml.jackson.annotation.JsonIgnore;
import dev.oak3.sbs4j.DeserializerBuffer;
import dev.oak3.sbs4j.SerializerBuffer;
import dev.oak3.sbs4j.exception.ValueDeserializationException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Base class for all types which have an array of child types
 *
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
@Setter
@Getter
@EqualsAndHashCode(callSuper = false, of = {"childTypes"})
public abstract class AbstractCLTypeWithChildren extends AbstractCLType {

    @JsonIgnore
    private List<AbstractCLType> childTypes = new ArrayList<>();
    private List<Object> childTypeObjects;

    protected void setChildTypeObjects(final List<Object> childTypeObjects)
            throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
            NoSuchMethodException, SecurityException, NoSuchTypeException {
        this.childTypeObjects = childTypeObjects;
        this.loadCLTypes(childTypeObjects);
    }

    public List<AbstractCLType> getChildTypes() {
        return childTypes == null ? this.childTypes = new ArrayList<>() : childTypes;
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
    public CLTypeData getChildClTypeData(final int index) throws NoSuchTypeException {
        return CLTypeData.getTypeByName(getChildTypes().get(index).getTypeName());
    }

    @Override
    public boolean isDeserializable() {
        return getChildTypes().stream().allMatch(AbstractCLType::isDeserializable);
    }

    protected void loadCLTypes(final List<Object> childTypeObjects)
            throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
            NoSuchMethodException, SecurityException, NoSuchTypeException {
        childTypes.clear();

        if (childTypeObjects != null) {
            for (Object childTypeObject : childTypeObjects) {
                addChildType(childTypeObject, childTypes);
            }
        }
    }

    private void addChildType(final Object childTypeObject, final List<AbstractCLType> parent)
            throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
            NoSuchMethodException, SecurityException, NoSuchTypeException {
        if (childTypeObject instanceof String) {
            parent.add(CLTypeData.getTypeByName(childTypeObject.toString()).getClTypeClass().getConstructor()
                    .newInstance());
        } else if (childTypeObject instanceof List) {
            for (Object child : (List<?>) childTypeObject) {
                addChildType(child, parent);
            }
        } else if (childTypeObject instanceof Map) {
            Map<?, ?> subChildTypes = (LinkedHashMap<?, ?>) childTypeObject;

            for (Entry<?, ?> entry : subChildTypes.entrySet()) {
                AbstractCLType nextParent = CLTypeData.getTypeByName(entry.getKey().toString()).getClTypeClass()
                        .getConstructor().newInstance();
                parent.add(nextParent);
                addChildType(entry.getValue(), ((AbstractCLTypeWithChildren) nextParent).getChildTypes());
            }
        }
    }

    @Override
    public void serialize(final SerializerBuffer ser) throws NoSuchTypeException {
        super.serialize(ser);
        serializeChildTypes(ser);
    }

    /**
     * Updates the child types from the deserializer buffer.
     *
     * @param deser the deserializer buffer
     */
    public abstract void deserializeChildTypes(final DeserializerBuffer deser)
            throws ValueDeserializationException, NoSuchTypeException, DynamicInstanceException;

    /**
     * Writes the child types to the serialization buffer.
     *
     * @param ser the serialization buffer
     */
    protected abstract void serializeChildTypes(final SerializerBuffer ser) throws NoSuchTypeException;

    protected AbstractCLType deserializeChildType(final DeserializerBuffer deser)
            throws ValueDeserializationException, NoSuchTypeException, DynamicInstanceException {
        final int childTypeTag = deser.readU8();
        final CLTypeData childType = CLTypeData.getTypeBySerializationTag((byte) childTypeTag);
        final AbstractCLType clChildType = CLTypeData.createCLTypeFromCLTypeName(childType.getClTypeName());

        if (clChildType instanceof AbstractCLTypeWithChildren) {
            ((AbstractCLTypeWithChildren) clChildType).deserializeChildTypes(deser);
        }

        return clChildType;
    }
}
