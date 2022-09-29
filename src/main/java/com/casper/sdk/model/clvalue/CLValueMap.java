package com.casper.sdk.model.clvalue;

import com.casper.sdk.exception.DynamicInstanceException;
import com.casper.sdk.exception.NoSuchTypeException;
import com.casper.sdk.model.clvalue.cltype.AbstractCLTypeWithChildren;
import com.casper.sdk.model.clvalue.cltype.CLTypeData;
import com.casper.sdk.model.clvalue.cltype.CLTypeMap;
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

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Casper Map CLValue implementation
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
public class CLValueMap extends
        AbstractCLValueWithChildren<Map<? extends AbstractCLValue<?, ?>, ? extends AbstractCLValue<?, ?>>, CLTypeMap> {
    @JsonProperty("cl_type")
    private CLTypeMap clType = new CLTypeMap();

    public CLValueMap(Map<? extends AbstractCLValue<?, ?>, ? extends AbstractCLValue<?, ?>> value) throws ValueSerializationException {
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

        CLValueI32 mapLength = new CLValueI32(getValue().size());
        mapLength.serialize(ser);

        for (Entry<? extends AbstractCLValue<?, ?>, ? extends AbstractCLValue<?, ?>> entry : getValue().entrySet()) {
            entry.getKey().serialize(ser);
            entry.getValue().serialize(ser);
        }

        if (target.equals(Target.BYTE)) {
            this.encodeType(ser);
        }

        this.setBytes(Hex.toHexString(ser.toByteArray()));
    }

    @Override
    public void deserialize(DeserializerBuffer deser) throws ValueDeserializationException {
        try {
            CLTypeData keyType = clType.getKeyValueTypes().getKeyType().getClTypeData();
            CLTypeData valType = clType.getKeyValueTypes().getValueType().getClTypeData();

            Map<AbstractCLValue<?, ?>, AbstractCLValue<?, ?>> map = new LinkedHashMap<>();
            CLValueI32 mapLength = new CLValueI32(0);
            mapLength.deserialize(deser);

            for (int i = 0; i < mapLength.getValue(); i++) {
                AbstractCLValue<?, ?> key = CLTypeData.createCLValueFromCLTypeData(keyType);
                if (key.getClType() instanceof AbstractCLTypeWithChildren) {
                    ((AbstractCLTypeWithChildren) key.getClType())
                            .setChildTypes(
                                    ((AbstractCLTypeWithChildren) clType.getKeyValueTypes().getKeyType()).getChildTypes());
                }
                key.deserialize(deser);

                AbstractCLValue<?, ?> val = CLTypeData.createCLValueFromCLTypeData(valType);

                if (val.getClType() instanceof CLTypeMap) {
                    ((CLTypeMap) val.getClType())
                            .setKeyValueTypes(((CLTypeMap) clType.getKeyValueTypes().getValueType()).getKeyValueTypes());
                } else if (val.getClType() instanceof AbstractCLTypeWithChildren) {
                    ((AbstractCLTypeWithChildren) val.getClType())
                            .setChildTypes(((AbstractCLTypeWithChildren) clType.getKeyValueTypes().getValueType())
                                    .getChildTypes());
                }
                val.deserialize(deser);

                map.put(key, val);
            }

            setValue(map);
        } catch (NoSuchTypeException | DynamicInstanceException | ValueSerializationException e) {
            throw new ValueDeserializationException(String.format("Error deserializing %s", this.getClass().getSimpleName()), e);
        }
    }

    @Override
    protected void setChildTypes(Map<? extends AbstractCLValue<?, ?>, ? extends AbstractCLValue<?, ?>> value) {
        Entry<? extends AbstractCLValue<?, ?>, ? extends AbstractCLValue<?, ?>> entry = value.entrySet().iterator()
                .next();

        clType.setKeyValueTypes(
                new CLTypeMap.CLTypeMapEntryType(entry.getKey().getClType(), entry.getValue().getClType()));
    }
}