package com.casper.sdk.model.clvalue;

import com.casper.sdk.exception.NoSuchTypeException;
import com.casper.sdk.model.clvalue.cltype.AbstractCLTypeWithChildren;
import com.casper.sdk.model.clvalue.cltype.CLTypeData;
import com.casper.sdk.model.clvalue.cltype.CLTypeList;
import com.casper.sdk.model.clvalue.cltype.CLTypeOption;
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

import java.util.Optional;

/**
 * Casper Option CLValue implementation
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
public class CLValueOption extends AbstractCLValueWithChildren<Optional<AbstractCLValue<?, ?>>, CLTypeOption> {
    @JsonProperty("cl_type")
    private CLTypeOption clType = new CLTypeOption();

    @JsonSetter("cl_type")
    public void setClType(CLTypeOption clType) {
        this.clType = clType;
        childTypesSet();
    }

    public CLValueOption(Optional<AbstractCLValue<?, ?>> value) throws ValueSerializationException {
        setChildTypes(value);
        this.setValue(value);
    }

    @Override
    public void serialize(SerializerBuffer ser, Target target) throws ValueSerializationException, NoSuchTypeException {
        if (!this.getValue().isPresent()) return;

        if (target.equals(Target.BYTE)) {
            super.serializePrefixWithLength(ser);
        }

        Optional<AbstractCLValue<?, ?>> value = getValue();

        CLValueBool isPresent = new CLValueBool(value.isPresent() && value.get().getValue() != null);
        isPresent.serialize(ser);

        Optional<AbstractCLValue<?, ?>> child = getValue();

        if (child.isPresent() && child.get().getClType() instanceof AbstractCLTypeWithChildren) {
            ((AbstractCLTypeWithChildren) child.get().getClType())
                    .setChildTypes(((AbstractCLTypeWithChildren) clType.getOptionType()).getChildTypes());
        }
        if (child.isPresent() && isPresent.getValue().equals(Boolean.TRUE)) {
            child.get().serialize(ser);
        }

        if (target.equals(Target.BYTE)) {
            this.encodeType(ser);
            if (child.isPresent()) {
                child.get().encodeType(ser);
            }
        }

        this.setBytes(Hex.toHexString(ser.toByteArray()));
    }

    @Override
    public void deserializeCustom(DeserializerBuffer deser) throws Exception {
        CLValueBool isPresent = new CLValueBool();
        isPresent.deserializeCustom(deser);

        CLTypeData childTypeData = clType.getOptionType().getClTypeData();

        AbstractCLValue<?, ?> child = CLTypeData.createCLValueFromCLTypeData(childTypeData);

        if (child.getClType() instanceof CLTypeList) {
            ((CLTypeList) child.getClType())
                    .setListType(((CLTypeList) clType.getOptionType()).getListType());
        } else if (child.getClType() instanceof AbstractCLTypeWithChildren) {
            ((AbstractCLTypeWithChildren) child.getClType())
                    .setChildTypes(((AbstractCLTypeWithChildren) clType.getOptionType()).getChildTypes());
        }

        if (isPresent.getValue().equals(Boolean.TRUE)) {
            child.deserializeCustom(deser);
        }

        setValue(Optional.of(child));
    }

    @Override
    protected void setChildTypes(Optional<AbstractCLValue<?, ?>> value) {
        if (value.isPresent()) {
            clType.setOptionType(value.get().getClType());
        } else {
            clType.setChildTypes(null);
        }
    }

    @Override
    public String toString() {
        return getValue() != null && getValue().isPresent() && getValue().get().getValue() != null ? getValue().get().toString() : "None";
    }
}
