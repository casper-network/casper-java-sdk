package com.casper.sdk.model.clvalue;

import com.casper.sdk.exception.DynamicInstanceException;
import com.casper.sdk.exception.NoSuchTypeException;
import com.casper.sdk.model.clvalue.cltype.AbstractCLTypeWithChildren;
import com.casper.sdk.model.clvalue.cltype.CLTypeData;
import com.casper.sdk.model.clvalue.cltype.CLTypeList;
import com.casper.sdk.model.clvalue.serde.Target;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import dev.oak3.sbs4j.DeserializerBuffer;
import dev.oak3.sbs4j.SerializerBuffer;
import dev.oak3.sbs4j.exception.ValueDeserializationException;
import dev.oak3.sbs4j.exception.ValueSerializationException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bouncycastle.util.encoders.Hex;

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
public class CLValueList extends AbstractCLValueWithChildren<List<? extends AbstractCLValue<?, ?>>, CLTypeList> {
    @JsonProperty("cl_type")
    private CLTypeList clType = new CLTypeList();

    public CLValueList(List<? extends AbstractCLValue<?, ?>> value) throws ValueSerializationException {
        setChildTypes(value);
        this.setValue(value);
    }

    @Override
    public void serialize(SerializerBuffer ser, Target target) throws ValueSerializationException, NoSuchTypeException {
        if (this.getValue() == null) return;

        if (target.equals(Target.BYTE)) {
            super.serializePrefixWithLength(ser);
        }

        setChildTypes(this.getValue());

        // List length is written first
        CLValueI32 length = new CLValueI32(getValue().size());
        length.serialize(ser);

        for (AbstractCLValue<?, ?> child : getValue()) {
            child.serialize(ser);
        }

        if (target.equals(Target.BYTE)) {
            this.encodeType(ser);
        }

        this.setBytes(Hex.toHexString(ser.toByteArray()));
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
        } catch (NoSuchTypeException | DynamicInstanceException | ValueSerializationException e) {
            throw new ValueDeserializationException(String.format("Error deserializing %s", this.getClass().getSimpleName()), e);
        }
    }

    @Override
    protected void setChildTypes(List<? extends AbstractCLValue<?, ?>> value) {
        clType.setListType(value.get(0).getClType());
    }
}
