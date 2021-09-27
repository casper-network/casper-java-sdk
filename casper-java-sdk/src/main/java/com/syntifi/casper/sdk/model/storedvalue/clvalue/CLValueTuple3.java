package com.syntifi.casper.sdk.model.storedvalue.clvalue;

import java.io.IOException;
import java.util.Arrays;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.syntifi.casper.sdk.exception.CLValueDecodeException;
import com.syntifi.casper.sdk.exception.CLValueEncodeException;
import com.syntifi.casper.sdk.exception.DynamicInstanceException;
import com.syntifi.casper.sdk.jackson.deserializer.CLValueTuple3Deserializer;
import com.syntifi.casper.sdk.jackson.serializer.CLValueTuple3Serializer;
import com.syntifi.casper.sdk.model.storedvalue.clvalue.encdec.CLValueDecoder;
import com.syntifi.casper.sdk.model.storedvalue.clvalue.encdec.CLValueEncoder;

import org.javatuples.Triplet;

/**
 * Casper Tuple3 CLValue implementation
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @see AbstractCLValue
 * @since 0.0.1
 */
@JsonSerialize(using = CLValueTuple3Serializer.class)
@JsonDeserialize(using = CLValueTuple3Deserializer.class)
public class CLValueTuple3 extends
        AbstractCLValue<Triplet<? extends AbstractCLValue<?>, ? extends AbstractCLValue<?>, ? extends AbstractCLValue<?>>> {

    public CLValueTuple3() {
        this(null);
    }

    public CLValueTuple3(
            Triplet<? extends AbstractCLValue<?>, ? extends AbstractCLValue<?>, ? extends AbstractCLValue<?>> value) {
        super(value,
                new CLType(CLTypeData.TUPLE3,
                        value == null ? null
                                : Arrays.asList(value.getValue0().getClType(), value.getValue1().getClType(),
                                        value.getValue2().getClType())));
    }

    @Override
    public void encode(CLValueEncoder clve) throws IOException, CLValueEncodeException, DynamicInstanceException {
        getValue().getValue0().encode(clve);
        getValue().getValue1().encode(clve);
        getValue().getValue2().encode(clve);

        setBytes(getValue().getValue0().getBytes() + getValue().getValue1().getBytes()
                + getValue().getValue2().getBytes());
    }

    @Override
    public void decode(CLValueDecoder clvd) throws IOException, CLValueDecodeException, DynamicInstanceException {
        CLType childTypeData1 = getClType().getChildTypes().get(0);
        CLType childTypeData2 = getClType().getChildTypes().get(1);
        CLType childTypeData3 = getClType().getChildTypes().get(2);

        AbstractCLValue<?> child1 = CLTypeData.createCLValueFromCLTypeData(childTypeData1.getClTypeData());
        child1.setClType(childTypeData1);
        child1.decode(clvd);

        AbstractCLValue<?> child2 = CLTypeData.createCLValueFromCLTypeData(childTypeData2.getClTypeData());
        child2.setClType(childTypeData2);
        child2.decode(clvd);

        AbstractCLValue<?> child3 = CLTypeData.createCLValueFromCLTypeData(childTypeData3.getClTypeData());
        child3.setClType(childTypeData3);
        child3.decode(clvd);

        setValue(new Triplet<>(child1, child2, child3));
        setBytes(getValue().getValue0().getBytes() + getValue().getValue1().getBytes()
                + getValue().getValue2().getBytes());
    }
}
