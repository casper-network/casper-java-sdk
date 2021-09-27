package com.syntifi.casper.sdk.model.storedvalue.clvalue;

import java.io.IOException;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.syntifi.casper.sdk.exception.CLValueDecodeException;
import com.syntifi.casper.sdk.jackson.deserializer.CLValueUnitDeserializer;
import com.syntifi.casper.sdk.jackson.serializer.CLValueUnitSerializer;
import com.syntifi.casper.sdk.model.storedvalue.clvalue.encdec.CLValueDecoder;
import com.syntifi.casper.sdk.model.storedvalue.clvalue.encdec.CLValueEncoder;

/**
 * Casper Unit CLValue implementation
 * 
 * Unit is singleton value without additional semantics and serializes to an
 * empty byte array.
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @see AbstractCLValue
 * @since 0.0.1
 */
@JsonSerialize(using = CLValueUnitSerializer.class)
@JsonDeserialize(using = CLValueUnitDeserializer.class)
public class CLValueUnit extends AbstractCLValue<Object> {
    private static final String UNITY_EMPTY_VALUE = "";

    public CLValueUnit() {
        super(UNITY_EMPTY_VALUE, new CLType(CLTypeData.UNIT, null));
    }

    @Override
    public void encode(CLValueEncoder clve) throws IOException {
        setBytes("");
    }

    @Override
    public void decode(CLValueDecoder clvd) throws IOException, CLValueDecodeException {
        setBytes("");
    }
}