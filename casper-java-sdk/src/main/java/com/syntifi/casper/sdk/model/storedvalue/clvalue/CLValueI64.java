package com.syntifi.casper.sdk.model.storedvalue.clvalue;

import java.io.IOException;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.syntifi.casper.sdk.exception.CLValueDecodeException;
import com.syntifi.casper.sdk.jackson.deserializer.CLValueI64Deserializer;
import com.syntifi.casper.sdk.jackson.serializer.CLValueI64Serializer;
import com.syntifi.casper.sdk.model.storedvalue.clvalue.encdec.CLValueDecoder;
import com.syntifi.casper.sdk.model.storedvalue.clvalue.encdec.CLValueEncoder;

/**
 * Casper I64 CLValue implementation
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @see AbstractCLValue
 * @since 0.0.1
 */
@JsonSerialize(using = CLValueI64Serializer.class)
@JsonDeserialize(using = CLValueI64Deserializer.class)
public class CLValueI64 extends AbstractCLValue<Long> {

    public CLValueI64() {
        this(null);
    }

    public CLValueI64(Long value) {
        super(value, new CLType(CLTypeData.I64, null));
    }

    @Override
    public void encode(CLValueEncoder clve) throws IOException {
        clve.writeI64(this);
    }

    @Override
    public void decode(CLValueDecoder clvd) throws IOException, CLValueDecodeException {
        clvd.readI64(this);
    }
}
