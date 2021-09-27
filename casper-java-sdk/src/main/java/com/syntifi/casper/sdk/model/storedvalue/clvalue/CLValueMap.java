package com.syntifi.casper.sdk.model.storedvalue.clvalue;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.syntifi.casper.sdk.exception.CLValueDecodeException;
import com.syntifi.casper.sdk.exception.CLValueEncodeException;
import com.syntifi.casper.sdk.exception.DynamicInstanceException;
import com.syntifi.casper.sdk.jackson.deserializer.CLValueMapDeserializer;
import com.syntifi.casper.sdk.jackson.serializer.CLValueMapSerializer;
import com.syntifi.casper.sdk.model.storedvalue.clvalue.encdec.CLValueDecoder;
import com.syntifi.casper.sdk.model.storedvalue.clvalue.encdec.CLValueEncoder;

/**
 * Casper Map CLValue implementation
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @see AbstractCLValue
 * @since 0.0.1
 */
@JsonSerialize(using = CLValueMapSerializer.class)
@JsonDeserialize(using = CLValueMapDeserializer.class)
public class CLValueMap extends AbstractCLValue<Map<? extends AbstractCLValue<?>, ? extends AbstractCLValue<?>>> {

    public CLValueMap() {
        this(null);
    }

    public CLValueMap(Map<? extends AbstractCLValue<?>, ? extends AbstractCLValue<?>> value) {
        super(value, new CLType(CLTypeData.MAP, value == null ? null : getCLTypeDataOfChildren(value)));
    }

    @Override
    public void encode(CLValueEncoder clve) throws IOException, CLValueEncodeException, DynamicInstanceException {
        CLValueI32 mapLength = new CLValueI32(getValue().size());
        clve.writeI32(mapLength);
        setBytes(mapLength.getBytes());

        for (Entry<? extends AbstractCLValue<?>, ? extends AbstractCLValue<?>> entry : getValue().entrySet()) {
            entry.getKey().encode(clve);
            entry.getValue().encode(clve);
            setBytes(getBytes() + entry.getKey().getBytes() + entry.getValue().getBytes());
        }
    }

    @Override
    public void decode(CLValueDecoder clvd) throws IOException, CLValueDecodeException, DynamicInstanceException {
        CLType keyType = getClType().getChildTypes().get(0);
        CLType valType = getClType().getChildTypes().get(1);

        Map<AbstractCLValue<?>, AbstractCLValue<?>> map = new HashMap<>();
        CLValueI32 mapLength = new CLValueI32(0);
        clvd.readI32(mapLength);

        for (int i = 0; i < mapLength.getValue(); i++) {
            AbstractCLValue<?> key = CLTypeData.createCLValueFromCLTypeData(keyType.getClTypeData());
            key.setClType(keyType);
            key.decode(clvd);

            AbstractCLValue<?> val = CLTypeData.createCLValueFromCLTypeData(valType.getClTypeData());
            val.setClType(valType);
            val.decode(clvd);

            map.put(key, val);
        }

        setValue(map);
    }

    static List<CLType> getCLTypeDataOfChildren(Map<? extends AbstractCLValue<?>, ? extends AbstractCLValue<?>> value) {
        Entry<? extends AbstractCLValue<?>, ? extends AbstractCLValue<?>> entry = (Entry<? extends AbstractCLValue<?>, ? extends AbstractCLValue<?>>) value
                .entrySet().toArray()[0];

        return Arrays.asList(entry.getKey().getClType(), entry.getValue().getClType());
    }
}