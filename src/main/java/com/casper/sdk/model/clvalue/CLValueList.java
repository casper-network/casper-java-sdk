package com.casper.sdk.model.clvalue;

import com.casper.sdk.exception.DynamicInstanceException;
import com.casper.sdk.exception.NoSuchTypeException;
import com.casper.sdk.model.clvalue.cltype.AbstractCLTypeWithChildren;
import com.casper.sdk.model.clvalue.cltype.CLTypeData;
import com.casper.sdk.model.clvalue.cltype.CLTypeList;
import com.fasterxml.jackson.annotation.JsonProperty;
import dev.oak3.sbs4j.DeserializerBuffer;
import dev.oak3.sbs4j.SerializerBuffer;
import dev.oak3.sbs4j.exception.ValueDeserializationException;
import dev.oak3.sbs4j.exception.ValueSerializationException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.LinkedList;
import java.util.List;

/**
 * Casper List CLValue implementation
 *
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @see AbstractCLValue
 * @since 0.0.1
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class CLValueList extends AbstractCLValue<List<? extends AbstractCLValue<?, ?>>, CLTypeList> {
    @JsonProperty("cl_type")
    private CLTypeList clType = new CLTypeList();

    public CLValueList(List<? extends AbstractCLValue<?, ?>> value) {
        this.setValue(value);
        setListType();
    }

    @Override
    public void serialize(SerializerBuffer ser, boolean encodeType) throws ValueSerializationException, NoSuchTypeException {
        if (this.getValue() == null) return;

        setListType();

        // List length is written first
        CLValueI32 length = new CLValueI32(getValue().size());
        length.serialize(ser);

        for (AbstractCLValue<?, ?> child : getValue()) {
            child.serialize(ser);
        }

        if (encodeType) {
            this.encodeType(ser);
        }
    }

    @Override
    public void deserialize(DeserializerBuffer deser) throws ValueDeserializationException {
        try {
            CLTypeData childrenType = getClType().getListType().getClTypeData();

            // List length is sent first
            CLValueI32 length = new CLValueI32();
            length.deserialize(deser);

            List<AbstractCLValue<?, ?>> list = new LinkedList<>();
            for (int i = 0; i < length.getValue(); i++) {
                AbstractCLValue<?, ?> child = CLTypeData.createCLValueFromCLTypeData(childrenType);
                if (child.getClType() instanceof AbstractCLTypeWithChildren) {
                    ((AbstractCLTypeWithChildren) child.getClType())
                            .setChildTypes(((AbstractCLTypeWithChildren) clType.getListType()).getChildTypes());
                }
                child.deserialize(deser);
                list.add(child);
            }

            setValue(list);
        } catch (NoSuchTypeException | DynamicInstanceException e) {
            throw new ValueDeserializationException(String.format("Error deserializing %s", this.getClass().getSimpleName()), e);
        }
    }

    protected void setListType() {
        clType.setListType(getValue().get(0).getClType());
    }
}
