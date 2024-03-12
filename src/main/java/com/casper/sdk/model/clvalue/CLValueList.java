package com.casper.sdk.model.clvalue;

import com.casper.sdk.model.clvalue.cltype.AbstractCLTypeWithChildren;
import com.casper.sdk.model.clvalue.cltype.CLTypeData;
import com.casper.sdk.model.clvalue.cltype.CLTypeList;
import com.casper.sdk.model.clvalue.cltype.CLTypeMap;
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
    public void setClType(final CLTypeList clType) {
        this.clType = clType;
        childTypesSet();
    }

    public CLValueList(final List<? extends AbstractCLValue<?, ?>> value) throws ValueSerializationException {
        setChildTypes(value);
        this.setValue(value);
    }

    @Override
    protected void serializeValue(final SerializerBuffer ser) throws ValueSerializationException {

        final SerializerBuffer serVal = new SerializerBuffer();

        setChildTypes(this.getValue());

        // List length is written first
        final CLValueI32 length = new CLValueI32(getValue().size());
        length.serialize(serVal);

        for (AbstractCLValue<?, ?> child : getValue()) {
            child.serialize(serVal);
        }

        final byte[] bytes = serVal.toByteArray();
        ser.writeByteArray(bytes);
        this.setBytes(Hex.toHexString(bytes));
    }


    @Override
    public void deserializeCustom(final DeserializerBuffer deser) throws Exception {
        final CLTypeData childrenType = getClType().getListType().getClTypeData();

        // List length is sent first
        final CLValueI32 length = new CLValueI32();
        length.deserializeCustom(deser);

        final List<AbstractCLValue<?, ?>> list = new ArrayList<>();
        for (int i = 0; i < length.getValue(); i++) {
            final AbstractCLValue<?, ?> child = CLTypeData.createCLValueFromCLTypeData(childrenType);
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
    protected void setChildTypes(final List<? extends AbstractCLValue<?, ?>> value) {
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
