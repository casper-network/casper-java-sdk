package com.syntifi.casper.sdk.model.storedvalue.clvalue;

import java.io.IOException;
import java.math.BigInteger;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.syntifi.casper.sdk.exception.CLValueDecodeException;
import com.syntifi.casper.sdk.exception.CLValueEncodeException;
import com.syntifi.casper.sdk.jackson.deserializer.CLValueU64Deserializer;
import com.syntifi.casper.sdk.jackson.serializer.CLValueU64Serializer;
import com.syntifi.casper.sdk.model.storedvalue.clvalue.encdec.CLValueDecoder;
import com.syntifi.casper.sdk.model.storedvalue.clvalue.encdec.CLValueEncoder;

/**
 * Casper U64 CLValue implementation
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @see AbstractCLValue
 * @since 0.0.1
 */
@JsonSerialize(using = CLValueU64Serializer.class)
@JsonDeserialize(using = CLValueU64Deserializer.class)
public class CLValueU64 extends AbstractCLValue<BigInteger> {

    public CLValueU64() {
        this(null);
    }

    public CLValueU64(BigInteger value) {
        super(value, new CLType(CLTypeData.U64, null));
    }

    @Override
    public void encode(CLValueEncoder clve) throws IOException, CLValueEncodeException {
        clve.writeU64(this);
    }

    @Override
    public void decode(CLValueDecoder clvd) throws IOException, CLValueDecodeException {
        clvd.readU64(this);
    }
}
