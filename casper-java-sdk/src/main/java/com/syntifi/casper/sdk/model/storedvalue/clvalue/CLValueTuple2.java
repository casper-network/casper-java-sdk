package com.syntifi.casper.sdk.model.storedvalue.clvalue;

import java.io.IOException;
import java.util.Arrays;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.syntifi.casper.sdk.exception.CLValueDecodeException;
import com.syntifi.casper.sdk.exception.CLValueEncodeException;
import com.syntifi.casper.sdk.exception.DynamicInstanceException;
import com.syntifi.casper.sdk.jackson.deserializer.CLValueTuple2Deserializer;
import com.syntifi.casper.sdk.jackson.serializer.CLValueTuple2Serializer;
import com.syntifi.casper.sdk.model.storedvalue.clvalue.encdec.CLValueDecoder;
import com.syntifi.casper.sdk.model.storedvalue.clvalue.encdec.CLValueEncoder;

import org.javatuples.Pair;

/**
 * Casper Tuple2 CLValue implementation
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @see AbstractCLValue
 * @since 0.0.1
 */
@JsonSerialize(using = CLValueTuple2Serializer.class)
@JsonDeserialize(using = CLValueTuple2Deserializer.class)
public class CLValueTuple2 extends AbstractCLValue<Pair<? extends AbstractCLValue<?>, ? extends AbstractCLValue<?>>> {

    public CLValueTuple2() {
        this(null);
    }

    public CLValueTuple2(Pair<? extends AbstractCLValue<?>, ? extends AbstractCLValue<?>> value) {
        super(value, new CLType(CLTypeData.TUPLE2,
                value == null ? null : Arrays.asList(value.getValue0().getClType(), value.getValue1().getClType())));
    }

    @Override
    public void encode(CLValueEncoder clve) throws IOException, CLValueEncodeException, DynamicInstanceException {
        getValue().getValue0().encode(clve);
        getValue().getValue1().encode(clve);

        setBytes(getValue().getValue0().getBytes() + getValue().getValue1().getBytes());
    }

    @Override
    public void decode(CLValueDecoder clvd) throws IOException, CLValueDecodeException, DynamicInstanceException {
        CLType childTypeData1 = getClType().getChildTypes().get(0);
        CLType childTypeData2 = getClType().getChildTypes().get(1);

        AbstractCLValue<?> child1 = CLTypeData.createCLValueFromCLTypeData(childTypeData1.getClTypeData());
        child1.setClType(childTypeData1);
        child1.decode(clvd);

        AbstractCLValue<?> child2 = CLTypeData.createCLValueFromCLTypeData(childTypeData2.getClTypeData());
        child2.setClType(childTypeData2);
        child2.decode(clvd);

        setValue(new Pair<>(child1, child2));
        setBytes(getValue().getValue0().getBytes() + getValue().getValue1().getBytes());
    }
}
