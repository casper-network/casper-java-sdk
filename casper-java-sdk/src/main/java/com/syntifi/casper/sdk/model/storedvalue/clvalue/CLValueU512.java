package com.syntifi.casper.sdk.model.storedvalue.clvalue;

import java.io.IOException;
import java.math.BigInteger;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.syntifi.casper.sdk.exception.CLValueDecodeException;
import com.syntifi.casper.sdk.exception.CLValueEncodeException;
import com.syntifi.casper.sdk.jackson.deserializer.CLValueU512Deserializer;
import com.syntifi.casper.sdk.jackson.serializer.CLValueU512Serializer;
import com.syntifi.casper.sdk.model.storedvalue.clvalue.encdec.CLValueDecoder;
import com.syntifi.casper.sdk.model.storedvalue.clvalue.encdec.CLValueEncoder;

/**
 * Casper U512 CLValue implementation
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @see AbstractCLValue
 * @since 0.0.1
 */
@JsonSerialize(using = CLValueU512Serializer.class)
@JsonDeserialize(using = CLValueU512Deserializer.class)
public class CLValueU512 extends AbstractCLValue<BigInteger> {

    public CLValueU512() {
        this(null);
    }

    public CLValueU512(BigInteger value) {
        super(value, new CLType(CLTypeData.U512, null));
    }

    @Override
    public void encode(CLValueEncoder clve) throws IOException, CLValueEncodeException {
        clve.writeU512(this);
    }

    @Override
    public void decode(CLValueDecoder clvd) throws IOException, CLValueDecodeException {
        clvd.readU512(this);
    }
}
