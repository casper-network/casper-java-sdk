package com.casper.sdk.model.clvalue;

import com.casper.sdk.exception.DynamicInstanceException;
import com.casper.sdk.exception.NoSuchTypeException;
import com.casper.sdk.model.clvalue.cltype.AbstractCLTypeWithChildren;
import com.casper.sdk.model.clvalue.cltype.CLTypeData;
import com.casper.sdk.model.clvalue.cltype.CLTypeOption;
import com.casper.sdk.model.clvalue.serde.Target;
import com.fasterxml.jackson.annotation.JsonProperty;
import dev.oak3.sbs4j.DeserializerBuffer;
import dev.oak3.sbs4j.SerializerBuffer;
import dev.oak3.sbs4j.exception.ValueDeserializationException;
import dev.oak3.sbs4j.exception.ValueSerializationException;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
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
@AllArgsConstructor
public class CLValueOption extends AbstractCLValue<Optional<AbstractCLValue<?, ?>>, CLTypeOption> {
    @JsonProperty("cl_type")
    private CLTypeOption clType = new CLTypeOption();

    public CLValueOption() {
        this(Optional.of(new CLValueAny(null)));
    }

    public CLValueOption(Optional<AbstractCLValue<?, ?>> value) {
        this.setValue(value);
        this.clType.setChildType(value.isPresent() ? value.get().getClType() : null);
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
                    .setChildTypes(((AbstractCLTypeWithChildren) clType.getChildType()).getChildTypes());
        }
        if (child.isPresent() && isPresent.getValue().equals(Boolean.TRUE)) {
            child.get().serialize(ser);
        }

        if (target.equals(Target.BYTE)) {
            this.encodeType(ser);
            if (child.isPresent() && isPresent.getValue().equals(Boolean.TRUE)) {
                child.get().encodeType(ser);
            }
        }

        this.setBytes(Hex.toHexString(ser.toByteArray()));
    }

    @Override
    public void deserialize(DeserializerBuffer deser) throws ValueDeserializationException {
        try {
            CLValueBool isPresent = new CLValueBool();
            isPresent.deserialize(deser);

            CLTypeData childTypeData = clType.getChildType().getClTypeData();

            AbstractCLValue<?, ?> child = CLTypeData.createCLValueFromCLTypeData(childTypeData);

            if (child.getClType() instanceof AbstractCLTypeWithChildren) {
                ((AbstractCLTypeWithChildren) child.getClType())
                        .setChildTypes(((AbstractCLTypeWithChildren) clType.getChildType()).getChildTypes());
            }

            setValue(Optional.of(child));

            if (isPresent.getValue().equals(Boolean.TRUE)) {
                child.deserialize(deser);
            }
        } catch (NoSuchTypeException | DynamicInstanceException e) {
            throw new ValueDeserializationException(String.format("Error deserializing %s", this.getClass().getSimpleName()), e);
        }
    }
}
