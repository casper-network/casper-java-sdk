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
    public void setClType(final CLTypeOption clType) {
        this.clType = clType;
        childTypesSet();
    }

    public CLValueOption(final Optional<AbstractCLValue<?, ?>> value) throws ValueSerializationException {
        setChildTypes(value);
        this.setValue(value);
    }

    @Override
    public void serialize(final SerializerBuffer ser, final Target target) throws ValueSerializationException, NoSuchTypeException {

        super.serialize(ser, target);

        if (target.equals(Target.BYTE)) {
            Optional<AbstractCLValue<?, ?>> child = getValue();
            if (child.isPresent()) {
                child.get().encodeType(ser);
            }
        }
    }

    @Override
    protected void serializeValue(final SerializerBuffer ser) throws ValueSerializationException {

        final SerializerBuffer serVal = new SerializerBuffer();
        final Optional<AbstractCLValue<?, ?>> value = getValue();

        final CLValueBool isPresent = new CLValueBool(value.isPresent() && value.get().getValue() != null);
        isPresent.serialize(serVal);

        if (value.isPresent() && value.get().getClType() instanceof AbstractCLTypeWithChildren) {
            ((AbstractCLTypeWithChildren) value.get().getClType())
                    .setChildTypes(((AbstractCLTypeWithChildren) clType.getOptionType()).getChildTypes());
        }
        if (value.isPresent() && isPresent.getValue().equals(Boolean.TRUE)) {
            value.get().serialize(serVal);
        }

        final byte[] bytes = serVal.toByteArray();
        ser.writeByteArray(bytes);
        this.setBytes(Hex.toHexString(bytes));
    }

    @Override
    public void deserializeCustom(final DeserializerBuffer deser) throws Exception {
        final CLValueBool isPresent = new CLValueBool();
        isPresent.deserializeCustom(deser);

        final CLTypeData childTypeData = clType.getOptionType().getClTypeData();

        final AbstractCLValue<?, ?> child = CLTypeData.createCLValueFromCLTypeData(childTypeData);

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
    protected void setChildTypes(final Optional<AbstractCLValue<?, ?>> value) {
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
