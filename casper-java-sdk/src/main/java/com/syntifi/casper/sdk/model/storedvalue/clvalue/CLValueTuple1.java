package com.syntifi.casper.sdk.model.storedvalue.clvalue;

import java.io.IOException;
import java.util.Arrays;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.syntifi.casper.sdk.exception.CLValueDecodeException;
import com.syntifi.casper.sdk.exception.CLValueEncodeException;
import com.syntifi.casper.sdk.exception.DynamicInstanceException;
import com.syntifi.casper.sdk.jackson.deserializer.CLValueTuple1Deserializer;
import com.syntifi.casper.sdk.jackson.serializer.CLValueTuple1Serializer;
import com.syntifi.casper.sdk.model.storedvalue.clvalue.encdec.CLValueDecoder;
import com.syntifi.casper.sdk.model.storedvalue.clvalue.encdec.CLValueEncoder;

import org.javatuples.Unit;

/**
 * Casper Tuple1 CLValue implementation
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @see AbstractCLValue
 * @since 0.0.1
 */
@JsonSerialize(using = CLValueTuple1Serializer.class)
@JsonDeserialize(using = CLValueTuple1Deserializer.class)
public class CLValueTuple1 extends AbstractCLValue<Unit<? extends AbstractCLValue<?>>> {

    public CLValueTuple1() {
        this(null);
    }

    public CLValueTuple1(Unit<? extends AbstractCLValue<?>> value) {
        super(value, new CLType(CLTypeData.TUPLE1, value == null ? null : Arrays.asList(value.getValue0().getClType())));
    }

    @Override
    public void encode(CLValueEncoder clve) throws IOException, CLValueEncodeException, DynamicInstanceException {
        getValue().getValue0().encode(clve);

        setBytes(getValue().getValue0().getBytes());
    }

    @Override
    public void decode(CLValueDecoder clvd) throws IOException, CLValueDecodeException, DynamicInstanceException {
        CLType childTypeData1 = getClType().getChildTypes().get(0);

        AbstractCLValue<?> child1 = CLTypeData.createCLValueFromCLTypeData(childTypeData1.getClTypeData());
        child1.setClType(childTypeData1);
        child1.decode(clvd);

        setValue(new Unit<>(child1));
        setBytes(getValue().getValue0().getBytes());
    }
}
