package com.syntifi.casper.sdk.model.storedvalue.clvalue;

import java.io.IOException;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.syntifi.casper.sdk.exception.CLValueDecodeException;
import com.syntifi.casper.sdk.jackson.deserializer.CLValueI32Deserializer;
import com.syntifi.casper.sdk.jackson.serializer.CLValueI32Serializer;
import com.syntifi.casper.sdk.model.storedvalue.clvalue.encdec.CLValueDecoder;
import com.syntifi.casper.sdk.model.storedvalue.clvalue.encdec.CLValueEncoder;

/**
 * Casper I32 CLValue implementation
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @see AbstractCLValue
 * @since 0.0.1
 */
@JsonSerialize(using = CLValueI32Serializer.class)
@JsonDeserialize(using = CLValueI32Deserializer.class)
public class CLValueI32 extends AbstractCLValue<Integer> {

    public CLValueI32() {
        this(null);
    }

    public CLValueI32(Integer value) {
        super(value, new CLType(CLTypeData.I32, null));
    }

    @Override
    public void encode(CLValueEncoder clve) throws IOException {
        clve.writeI32(this);
    }

    @Override
    public void decode(CLValueDecoder clvd) throws IOException, CLValueDecodeException {
        clvd.readI32(this);
    }
}
