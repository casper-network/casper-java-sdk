package com.casper.sdk.model.clvalue;

import com.casper.sdk.exception.NoSuchTypeException;
import com.casper.sdk.model.clvalue.cltype.AbstractCLTypeWithChildren;
import com.casper.sdk.model.clvalue.cltype.CLTypeData;
import com.casper.sdk.model.clvalue.cltype.CLTypeList;
import com.casper.sdk.model.clvalue.cltype.CLTypeMap;
import com.casper.sdk.model.clvalue.serde.Target;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import dev.oak3.sbs4j.DeserializerBuffer;
import dev.oak3.sbs4j.SerializerBuffer;
import dev.oak3.sbs4j.exception.ValueSerializationException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bouncycastle.util.encoders.Hex;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
public class CLValueList extends AbstractCLValueWithChildren<List<? extends AbstractCLValue<?, ?>>, CLTypeList> {
    @JsonProperty("cl_type")
    private CLTypeList clType = new CLTypeList();

    @JsonSetter("cl_type")
    public void setClType(CLTypeList clType) {
        this.clType = clType;
        childTypesSet();
    }

    public CLValueList(List<? extends AbstractCLValue<?, ?>> value) throws ValueSerializationException {
        setChildTypes(value);
        this.setValue(value);
    }

    @Override
    protected void serializeValue(final SerializerBuffer ser) throws ValueSerializationException {

        setChildTypes(this.getValue());

        // List length is written first
        CLValueI32 length = new CLValueI32(getValue().size());
        length.serialize(ser);

        for (AbstractCLValue<?, ?> child : getValue()) {
            child.serialize(ser);
        }

        this.setBytes(Hex.toHexString(ser.toByteArray()));
    }

    @Override
    protected void encodeType(SerializerBuffer ser) throws NoSuchTypeException {
        super.encodeType(ser);

        byte val = (getClType().getListType().getClTypeData().getSerializationTag());
        ser.writeU8(val);
    }

    @Override
    public void deserializeCustom(DeserializerBuffer deser) throws Exception {
        CLTypeData childrenType = getClType().getListType().getClTypeData();

        // List length is sent first
        CLValueI32 length = new CLValueI32();
        length.deserializeCustom(deser);

        List<AbstractCLValue<?, ?>> list = new ArrayList<>();
        for (int i = 0; i < length.getValue(); i++) {
            AbstractCLValue<?, ?> child = CLTypeData.createCLValueFromCLTypeData(childrenType);
            if (child.getClType() instanceof CLTypeMap) {
                ((CLTypeMap) child.getClType())
                        .setKeyValueTypes(((CLTypeMap) clType.getListType()).getKeyValueTypes());
            } else if (child.getClType() instanceof AbstractCLTypeWithChildren) {
                ((AbstractCLTypeWithChildren) child.getClType())
                        .setChildTypes(((AbstractCLTypeWithChildren) clType.getListType()).getChildTypes());
            }
            child.deserializeCustom(deser);
            list.add(child);
        }

        setValue(list);
    }

    @Override
    protected void setChildTypes(List<? extends AbstractCLValue<?, ?>> value) {
        if (!value.isEmpty()) {
            clType.setListType(value.get(0).getClType());
        } else {
            clType.setChildTypes(new ArrayList<>());
        }
    }

    @Override
    public String toString() {
        return getValue() != null ? getValue().stream().map(item -> item.getValue().toString()).collect(Collectors.joining(", ")) : null;
    }
}
