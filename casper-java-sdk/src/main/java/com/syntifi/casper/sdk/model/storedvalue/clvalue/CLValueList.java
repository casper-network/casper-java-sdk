package com.syntifi.casper.sdk.model.storedvalue.clvalue;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.syntifi.casper.sdk.exception.CLValueDecodeException;
import com.syntifi.casper.sdk.exception.CLValueEncodeException;
import com.syntifi.casper.sdk.exception.DynamicInstanceException;
import com.syntifi.casper.sdk.jackson.deserializer.CLValueListDeserializer;
import com.syntifi.casper.sdk.jackson.serializer.CLValueListSerializer;
import com.syntifi.casper.sdk.model.storedvalue.clvalue.encdec.CLValueDecoder;
import com.syntifi.casper.sdk.model.storedvalue.clvalue.encdec.CLValueEncoder;

/**
 * Casper List CLValue implementation
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @see AbstractCLValue
 * @since 0.0.1
 */
@JsonSerialize(using = CLValueListSerializer.class)
@JsonDeserialize(using = CLValueListDeserializer.class)
public class CLValueList extends AbstractCLValue<List<? extends AbstractCLValue<?>>> {

    public CLValueList() {
        this(null);
    }

    public CLValueList(List<? extends AbstractCLValue<?>> value) {
        super(value, new CLType(CLTypeData.LIST, value == null ? null : Arrays.asList(value.get(0).getClType())));
    }

    @Override
    public void encode(CLValueEncoder clve) throws IOException, CLValueEncodeException, DynamicInstanceException {
        // List length is written first
        CLValueI32 length = new CLValueI32(getValue().size());
        clve.writeI32(length);
        setBytes(length.getBytes());

        for (AbstractCLValue<?> child : getValue()) {
            child.encode(clve);
            setBytes(getBytes() + child.getBytes());
        }
    }

    @Override
    public void decode(CLValueDecoder clvd) throws IOException, CLValueDecodeException, DynamicInstanceException {
        CLType childrenType = getClType().getChildTypes().get(0);

        // List length is sent first
        CLValueI32 length = new CLValueI32(0);
        clvd.readI32(length);
        setBytes(length.getBytes());

        List<AbstractCLValue<?>> list = new LinkedList<>();
        for (int i = 0; i < length.getValue(); i++) {
            AbstractCLValue<?> child = CLTypeData.createCLValueFromCLTypeData(childrenType.getClTypeData());
            child.setClType(childrenType);
            child.decode(clvd);
            setBytes(getBytes() + child.getBytes());
            list.add(child);
        }

        setValue(list);
    }
}
