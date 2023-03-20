package com.casper.sdk.model.clvalue;

import com.casper.sdk.exception.NoSuchTypeException;
import com.casper.sdk.model.clvalue.cltype.AbstractCLTypeWithChildren;
import com.casper.sdk.model.clvalue.cltype.CLTypeData;
import com.casper.sdk.model.clvalue.cltype.CLTypeFixedList;
import com.casper.sdk.model.clvalue.serde.Target;
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
public class CLValueFixedList extends AbstractCLValue<List<? extends AbstractCLValue<?, ?>>, CLTypeFixedList> {
    @JsonProperty("cl_type")
    private CLTypeFixedList clType = new CLTypeFixedList();

    public CLValueFixedList(List<? extends AbstractCLValue<?, ?>> value) throws ValueSerializationException {
        setListType(value);
        this.setValue(value);
    }

    @Override
    public void serialize(SerializerBuffer ser, Target target) throws ValueSerializationException, NoSuchTypeException {
        if (this.getValue() == null) return;

        if (target.equals(Target.BYTE)) {
            super.serializePrefixWithLength(ser);
        }

        setListType(this.getValue());

        for (AbstractCLValue<?, ?> child : getValue()) {
            child.serialize(ser);
        }

        if (target.equals(Target.BYTE)) {
            this.encodeType(ser);
        }

        this.setBytes(Hex.toHexString(ser.toByteArray()));
    }

    @Override
    public void deserializeCustom(DeserializerBuffer deser) throws Exception {
        CLTypeData childrenType = getClType().getListType().getClTypeData();

        List<AbstractCLValue<?, ?>> list = new LinkedList<>();

        boolean hasMoreItems = true;
        do {
            AbstractCLValue<?, ?> child = CLTypeData.createCLValueFromCLTypeData(childrenType);
            if (child.getClType() instanceof AbstractCLTypeWithChildren) {
                ((AbstractCLTypeWithChildren) child.getClType())
                        .setChildTypes(((AbstractCLTypeWithChildren) clType.getListType()).getChildTypes());
            }
            try {
                child.deserializeCustom(deser);
                list.add(child);
            } catch (ValueDeserializationException valueDeserializationException) {
                hasMoreItems = false;
                if (deser.getBuffer().hasRemaining()) {
                    throw valueDeserializationException;
                }
            }
        } while (hasMoreItems);

        setValue(list);
    }

    protected void setListType(List<? extends AbstractCLValue<?, ?>> value) {
        clType.setListType(value.get(0).getClType());
    }

    @Override
    public String toString() {
        return getValue() != null ? getValue().stream().map(item -> item.getValue().toString()).collect(Collectors.joining(", ")) : null;
    }
}
