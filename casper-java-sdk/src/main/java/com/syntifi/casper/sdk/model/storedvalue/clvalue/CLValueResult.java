package com.syntifi.casper.sdk.model.storedvalue.clvalue;

import java.io.IOException;
import java.util.Arrays;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.syntifi.casper.sdk.exception.CLValueDecodeException;
import com.syntifi.casper.sdk.exception.CLValueEncodeException;
import com.syntifi.casper.sdk.exception.DynamicInstanceException;
import com.syntifi.casper.sdk.jackson.deserializer.CLValueResultDeserializer;
import com.syntifi.casper.sdk.jackson.serializer.CLValueResultSerializer;
import com.syntifi.casper.sdk.model.Result;
import com.syntifi.casper.sdk.model.storedvalue.clvalue.encdec.CLValueDecoder;
import com.syntifi.casper.sdk.model.storedvalue.clvalue.encdec.CLValueEncoder;

/**
 * Casper Result CLValue implementation
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @see AbstractCLValue
 * @since 0.0.1
 */
@JsonSerialize(using = CLValueResultSerializer.class)
@JsonDeserialize(using = CLValueResultDeserializer.class)
public class CLValueResult extends AbstractCLValue<Result> {

    public CLValueResult() {
        this(null);
    }

    public CLValueResult(Result value) {
        super(value, new CLType(CLTypeData.RESULT,
                value == null ? null : Arrays.asList(value.getOk().getClType(), value.getErr().getClType())));
    }

    @Override
    public void encode(CLValueEncoder clve) throws IOException, CLValueEncodeException, DynamicInstanceException {
        CLValueBool clValueTrue = new CLValueBool(true);
        clValueTrue.encode(clve);

        getValue().getOk().encode(clve);

        CLValueBool clValueFalse = new CLValueBool(false);
        clValueFalse.encode(clve);

        getValue().getErr().encode(clve);

        setBytes(clValueTrue.getBytes() + getValue().getOk().getBytes() + clValueFalse.getBytes()
                + getValue().getErr().getBytes());
    }

    @Override
    public void decode(CLValueDecoder clvd) throws IOException, CLValueDecodeException, DynamicInstanceException {
        Result result = new Result();

        for (int i = 0; i < 2; i++) {
            CLValueBool bool = new CLValueBool();
            bool.decode(clvd);

            CLType type = getClType().getChildTypes().get(Boolean.TRUE.equals(bool.getValue()) ? 0 : 1);
            AbstractCLValue<?> okErr = CLTypeData.createCLValueFromCLTypeData(type.getClTypeData());
            okErr.setClType(type);
            okErr.decode(clvd);

            if (Boolean.TRUE.equals(bool.getValue())) {
                result.setOk(okErr);
            } else {
                result.setErr(okErr);
            }
        }

        setValue(result);
    }
}