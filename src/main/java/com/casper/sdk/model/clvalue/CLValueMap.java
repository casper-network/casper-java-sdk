package com.casper.sdk.model.clvalue;

import com.casper.sdk.model.clvalue.cltype.AbstractCLTypeWithChildren;
import com.casper.sdk.model.clvalue.cltype.CLTypeData;
import com.casper.sdk.model.clvalue.cltype.CLTypeMap;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import dev.oak3.sbs4j.DeserializerBuffer;
import dev.oak3.sbs4j.SerializerBuffer;
import dev.oak3.sbs4j.exception.ValueSerializationException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bouncycastle.util.encoders.Hex;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.stream.Collectors;

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
@NoArgsConstructor
public class CLValueMap extends
        AbstractCLValueWithChildren<Map<? extends AbstractCLValue<?, ?>, ? extends AbstractCLValue<?, ?>>, CLTypeMap> {
    @JsonProperty("cl_type")
    private CLTypeMap clType = new CLTypeMap();

    @JsonSetter("cl_type")
    public void setClType(CLTypeMap clType) {
        this.clType = clType;
        childTypesSet();
    }

    public CLValueMap(final Map<? extends AbstractCLValue<?, ?>, ? extends AbstractCLValue<?, ?>> value) throws ValueSerializationException {
        setChildTypes(value);
        this.setValue(value);
    }


    @Override
    protected void serializeValue(final SerializerBuffer ser) throws ValueSerializationException {

        final SerializerBuffer serVal = new SerializerBuffer();

        setChildTypes(this.getValue());

        final CLValueI32 mapLength = new CLValueI32(getValue().size());
        mapLength.serialize(serVal);

        for (Entry<? extends AbstractCLValue<?, ?>, ? extends AbstractCLValue<?, ?>> entry : getValue().entrySet()) {
            entry.getKey().serialize(serVal);
            entry.getValue().serialize(serVal);
        }

        final byte[] bytes = serVal.toByteArray();
        ser.writeByteArray(bytes);
        this.setBytes(Hex.toHexString(bytes));
    }


    @Override
    public void deserializeCustom(final DeserializerBuffer deser) throws Exception {
        if (this.clType.getChildTypes().isEmpty()) {
            this.clType.deserializeChildTypes(deser);
        }

        final CLTypeData keyType = clType.getKeyValueTypes().getKeyType().getClTypeData();
        final CLTypeData valType = clType.getKeyValueTypes().getValueType().getClTypeData();

        final Map<AbstractCLValue<?, ?>, AbstractCLValue<?, ?>> map = new LinkedHashMap<>();
        final CLValueI32 mapLength = new CLValueI32(0);
        mapLength.deserializeCustom(deser);

        for (int i = 0; i < mapLength.getValue(); i++) {
            final AbstractCLValue<?, ?> key = CLTypeData.createCLValueFromCLTypeData(keyType);
            if (key.getClType() instanceof AbstractCLTypeWithChildren) {
                ((AbstractCLTypeWithChildren) key.getClType())
                        .setChildTypes(
                                ((AbstractCLTypeWithChildren) clType.getKeyValueTypes().getKeyType()).getChildTypes());
            }
            key.deserializeCustom(deser);

            final AbstractCLValue<?, ?> val = CLTypeData.createCLValueFromCLTypeData(valType);

            if (val.getClType() instanceof CLTypeMap) {
                ((CLTypeMap) val.getClType())
                        .setKeyValueTypes(((CLTypeMap) clType.getKeyValueTypes().getValueType()).getKeyValueTypes());
            } else if (val.getClType() instanceof AbstractCLTypeWithChildren) {
                ((AbstractCLTypeWithChildren) val.getClType())
                        .setChildTypes(((AbstractCLTypeWithChildren) clType.getKeyValueTypes().getValueType())
                                .getChildTypes());
            }
            val.deserializeCustom(deser);

            map.put(key, val);
        }

        setValue(map);
    }


    @Override
    @JsonIgnore
    protected void setChildTypes(final Map<? extends AbstractCLValue<?, ?>, ? extends AbstractCLValue<?, ?>> value) {
        if (value != null && value.entrySet().iterator().hasNext()) {
            final Entry<? extends AbstractCLValue<?, ?>, ? extends AbstractCLValue<?, ?>> entry = value.entrySet().iterator().next();
            clType.setKeyValueTypes(new CLTypeMap.CLTypeMapEntryType(entry.getKey().getClType(), entry.getValue().getClType()));
        } else {
            clType.setChildTypes(new ArrayList<>());
        }
    }

    /**
     * This needed to be customized to ensure equality is being checked correctly.
     * The java Map equals method tries to get the "other" map entry's value by using "this" key object,
     * which then fails to find the object since they are "different" and returns always null.
     *
     * @param o the object to compare
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof CLValueMap)) return false;
        final CLValueMap other = (CLValueMap) o;
        if (!other.canEqual(this)) return false;

        final Object this$clType = this.getClType();
        final Object other$clType = other.getClType();
        if (!Objects.equals(this$clType, other$clType)) return false;

        final Object this$bytes = this.getBytes();
        final Object other$bytes = other.getBytes();
        if (!Objects.equals(this$bytes, other$bytes)) return false;
        final Map<? extends AbstractCLValue<?, ?>, ? extends AbstractCLValue<?, ?>> this$value = this.getValue();
        final Map<? extends AbstractCLValue<?, ?>, ? extends AbstractCLValue<?, ?>> other$value = other.getValue();

        for (Entry<? extends AbstractCLValue<?, ?>, ? extends AbstractCLValue<?, ?>> this$entry : this$value.entrySet()) {
            AbstractCLValue<?, ?> this$entryKey = this$entry.getKey();
            AbstractCLValue<?, ?> this$entryValue = this$entry.getValue();
            boolean found = false;
            for (Entry<? extends AbstractCLValue<?, ?>, ? extends AbstractCLValue<?, ?>> other$entry : other$value.entrySet()) {
                AbstractCLValue<?, ?> other$entryKey = other$entry.getKey();
                AbstractCLValue<?, ?> other$entryValue = other$entry.getValue();
                if (this$entryKey.equals(other$entryKey) && this$entryValue.equals(other$entryValue)) {
                    found = true;
                    break;
                }
            }
            if (!found) return false;
        }

        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof CLValueMap;
    }

    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = super.hashCode();
        final Object $clType = this.getClType();
        result = result * PRIME + ($clType == null ? 43 : $clType.hashCode());
        return result;
    }

    @Override
    public String toString() {
        if (getValue() == null) {
            return null;
        } else {
            return getValue()
                    .entrySet()
                    .stream()
                    .map(entry ->
                            entry.getKey().toString() + "=" + entry.getValue().toString()).collect(Collectors.joining(", ")
                    );
        }
    }
}
