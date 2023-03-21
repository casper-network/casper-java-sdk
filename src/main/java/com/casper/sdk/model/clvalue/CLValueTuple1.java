package com.casper.sdk.model.clvalue;

import com.casper.sdk.exception.NoSuchTypeException;
import com.casper.sdk.model.clvalue.cltype.AbstractCLTypeWithChildren;
import com.casper.sdk.model.clvalue.cltype.CLTypeData;
import com.casper.sdk.model.clvalue.cltype.CLTypeTuple1;
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
import org.javatuples.Unit;

import java.util.Arrays;

/**
 * Casper Tuple1 CLValue implementation
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
public class CLValueTuple1 extends AbstractCLValueWithChildren<Unit<? extends AbstractCLValue<?, ?>>, CLTypeTuple1> {
    @JsonProperty("cl_type")
    private CLTypeTuple1 clType = new CLTypeTuple1();

    @JsonSetter("cl_type")
    public void setClType(CLTypeTuple1 clType) {
        this.clType = clType;
        childTypesSet();
    }

    public CLValueTuple1(Unit<? extends AbstractCLValue<?, ?>> value) throws ValueSerializationException {
        setChildTypes(value);
        this.setValue(value);
    }

    @Override
    public void serialize(SerializerBuffer ser, Target target) throws NoSuchTypeException, ValueSerializationException {
        if (this.getValue() == null) return;

        if (target.equals(Target.BYTE)) {
            super.serializePrefixWithLength(ser);
        }

        setChildTypes(this.getValue());
        getValue().getValue0().serialize(ser);

        if (target.equals(Target.BYTE)) {
            this.encodeType(ser);
        }

        this.setBytes(Hex.toHexString(ser.toByteArray()));
    }

    @Override
    public void deserializeCustom(DeserializerBuffer deser) throws Exception {
        CLTypeData childTypeData1 = clType.getChildClTypeData(0);

        AbstractCLValue<?, ?> child1 = CLTypeData.createCLValueFromCLTypeData(childTypeData1);
        if (child1.getClType() instanceof AbstractCLTypeWithChildren) {
            ((AbstractCLTypeWithChildren) child1.getClType())
                    .setChildTypes(((AbstractCLTypeWithChildren) clType.getChildTypes().get(0)).getChildTypes());
        }
        child1.deserializeCustom(deser);

        setValue(new Unit<>(child1));
    }

    @Override
    protected void setChildTypes(Unit<? extends AbstractCLValue<?, ?>> value) {
        clType.setChildTypes(Arrays.asList(value.getValue0().getClType()));
    }

    @Override
    public String toString() {
        return getValue() != null ? getValue().toString() : null;
    }
}
