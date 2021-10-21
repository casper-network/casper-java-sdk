package com.syntifi.casper.sdk.model.clvalue;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.syntifi.casper.sdk.exception.CLValueDecodeException;
import com.syntifi.casper.sdk.exception.CLValueEncodeException;
import com.syntifi.casper.sdk.exception.DynamicInstanceException;
import com.syntifi.casper.sdk.exception.NoSuchTypeException;
import com.syntifi.casper.sdk.model.clvalue.cltype.AbstractCLTypeWithChildren;
import com.syntifi.casper.sdk.model.clvalue.cltype.CLTypeData;
import com.syntifi.casper.sdk.model.clvalue.cltype.CLTypeMap;
import com.syntifi.casper.sdk.model.clvalue.encdec.CLValueDecoder;
import com.syntifi.casper.sdk.model.clvalue.encdec.CLValueEncoder;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
public class CLValueMap extends AbstractCLValueWithChildren<Map<? extends AbstractCLValue<?, ?>, ? extends AbstractCLValue<?, ?>>, CLTypeMap> {
    @JsonProperty("cl_type")
    private CLTypeMap clType = new CLTypeMap();

    public CLValueMap(Map<? extends AbstractCLValue<?, ?>, ? extends AbstractCLValue<?, ?>> value) {
        this.setValue(value);
        setChildTypes();
    }

    @Override
    public void encode(CLValueEncoder clve)
            throws IOException, CLValueEncodeException, DynamicInstanceException, NoSuchTypeException {
        setChildTypes();

        CLValueI32 mapLength = new CLValueI32(getValue().size());
        mapLength.encode(clve);
        setBytes(mapLength.getBytes());

        for (Entry<? extends AbstractCLValue<?, ?>, ? extends AbstractCLValue<?, ?>> entry : getValue().entrySet()) {
            entry.getKey().encode(clve);
            entry.getValue().encode(clve);
            setBytes(getBytes() + entry.getKey().getBytes() + entry.getValue().getBytes());
        }
    }

    @Override
    public void decode(CLValueDecoder clvd)
            throws IOException, CLValueDecodeException, DynamicInstanceException, NoSuchTypeException {
        CLTypeData keyType = clType.getKeyValueTypes().getKeyType().getClTypeData();
        CLTypeData valType = clType.getKeyValueTypes().getValueType().getClTypeData();

        Map<AbstractCLValue<?, ?>, AbstractCLValue<?, ?>> map = new LinkedHashMap<>();
        CLValueI32 mapLength = new CLValueI32(0);
        mapLength.decode(clvd);

        for (int i = 0; i < mapLength.getValue(); i++) {
            AbstractCLValue<?, ?> key = CLTypeData.createCLValueFromCLTypeData(keyType);
            if (key.getClType() instanceof AbstractCLTypeWithChildren) {
                ((AbstractCLTypeWithChildren) key.getClType())
                        .setChildTypes(((AbstractCLTypeWithChildren) clType.getKeyValueTypes().getKeyType()).getChildTypes());
            }
            key.decode(clvd);

            AbstractCLValue<?, ?> val = CLTypeData.createCLValueFromCLTypeData(valType);
            if (val.getClType() instanceof AbstractCLTypeWithChildren) {
                ((AbstractCLTypeWithChildren) val.getClType())
                        .setChildTypes(((AbstractCLTypeWithChildren) clType.getKeyValueTypes().getValueType()).getChildTypes());
            }
            val.decode(clvd);

            map.put(key, val);
        }

        setValue(map);
    }

    @Override
    protected void setChildTypes() {
        Entry<? extends AbstractCLValue<?, ?>, ? extends AbstractCLValue<?, ?>> entry = getValue().entrySet().iterator().next();

        clType.setKeyValueTypes(
                clType.new CLTypeMapEntryType(entry.getKey().getClType(), entry.getValue().getClType()));
    }
}