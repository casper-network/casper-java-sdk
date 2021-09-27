package com.syntifi.casper.sdk.model.storedvalue.clvalue;

import java.io.IOException;
import java.math.BigInteger;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.syntifi.casper.sdk.exception.CLValueDecodeException;
import com.syntifi.casper.sdk.exception.CLValueEncodeException;
import com.syntifi.casper.sdk.jackson.deserializer.CLValueU128Deserializer;
import com.syntifi.casper.sdk.jackson.serializer.CLValueU128Serializer;
import com.syntifi.casper.sdk.model.storedvalue.clvalue.encdec.CLValueDecoder;
import com.syntifi.casper.sdk.model.storedvalue.clvalue.encdec.CLValueEncoder;

/**
 * Casper U128 CLValue implementation
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @see AbstractCLValue
 * @since 0.0.1
 */
@JsonSerialize(using = CLValueU128Serializer.class)
@JsonDeserialize(using = CLValueU128Deserializer.class)
public class CLValueU128 extends AbstractCLValue<BigInteger> {

    public CLValueU128() {
        this(null);
    }

    public CLValueU128(BigInteger value) {
        super(value, new CLType(CLTypeData.U128, null));
    }

    @Override
    public void encode(CLValueEncoder clve) throws IOException, CLValueEncodeException {
        clve.writeU128(this);
    }

    @Override
    public void decode(CLValueDecoder clvd) throws IOException, CLValueDecodeException {
        clvd.readU128(this);
    }
}
