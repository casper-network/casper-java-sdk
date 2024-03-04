package com.casper.sdk.model.clvalue;

import com.casper.sdk.exception.NoSuchTypeException;
import com.casper.sdk.model.clvalue.cltype.AbstractCLTypeWithChildren;
import com.casper.sdk.model.clvalue.cltype.CLTypeData;
import com.casper.sdk.model.clvalue.cltype.CLTypeTuple2;
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
import org.javatuples.Pair;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Casper Tuple2 CLValue implementation
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
public class CLValueTuple2
        extends AbstractCLValueWithChildren<Pair<? extends AbstractCLValue<?, ?>, ? extends AbstractCLValue<?, ?>>, CLTypeTuple2> {
    @JsonProperty("cl_type")
    private CLTypeTuple2 clType = new CLTypeTuple2();

    @JsonSetter("cl_type")
    public void setClType(CLTypeTuple2 clType) {
        this.clType = clType;
        childTypesSet();
    }

    public CLValueTuple2(Pair<? extends AbstractCLValue<?, ?>, ? extends AbstractCLValue<?, ?>> value) throws ValueSerializationException {
        setChildTypes(value);
        this.setValue(value);
    }

    @Override
    protected void serializeValue(final SerializerBuffer ser) throws ValueSerializationException {
        setChildTypes(this.getValue());
        getValue().getValue0().serialize(ser);
        getValue().getValue1().serialize(ser);
        this.setBytes(Hex.toHexString(ser.toByteArray()));
    }

    @Override
    protected void encodeType(SerializerBuffer ser) throws NoSuchTypeException {
        super.encodeType(ser);

        byte element0TypeTag = getClType().getChildClTypeData(0).getSerializationTag();
        ser.writeU8(element0TypeTag);
        byte element1TypeTag = getClType().getChildClTypeData(1).getSerializationTag();
        ser.writeU8(element1TypeTag);
    }

    @Override
    public void deserializeCustom(DeserializerBuffer deser) throws Exception {
        CLTypeData childTypeData1 = clType.getChildClTypeData(0);
        CLTypeData childTypeData2 = clType.getChildClTypeData(1);

        AbstractCLValue<?, ?> child1 = CLTypeData.createCLValueFromCLTypeData(childTypeData1);
        if (child1.getClType() instanceof AbstractCLTypeWithChildren) {
            ((AbstractCLTypeWithChildren) child1.getClType())
                    .setChildTypes(((AbstractCLTypeWithChildren) clType.getChildTypes().get(0)).getChildTypes());
        }
        child1.deserializeCustom(deser);

        AbstractCLValue<?, ?> child2 = CLTypeData.createCLValueFromCLTypeData(childTypeData2);
        if (child2.getClType() instanceof AbstractCLTypeWithChildren) {
            ((AbstractCLTypeWithChildren) child2.getClType())
                    .setChildTypes(((AbstractCLTypeWithChildren) clType.getChildTypes().get(1)).getChildTypes());
        }
        child2.deserializeCustom(deser);

        setValue(new Pair<>(child1, child2));
    }

    @Override
    protected void setChildTypes(Pair<? extends AbstractCLValue<?, ?>, ? extends AbstractCLValue<?, ?>> value) {
        if (value.getValue0() != null && value.getValue1() != null) {
            clType.setChildTypes(Arrays.asList(value.getValue0().getClType(), value.getValue1().getClType()));
        } else {
            clType.setChildTypes(new ArrayList<>());
        }
    }

    @Override
    public String toString() {
        return getValue() != null ? getValue().toString() : null;
    }
}
