package com.syntifi.casper.sdk.model.storedvalue.clvalue;

import java.io.IOException;
import java.math.BigInteger;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.syntifi.casper.sdk.exception.CLValueDecodeException;
import com.syntifi.casper.sdk.exception.CLValueEncodeException;
import com.syntifi.casper.sdk.jackson.deserializer.CLValueU256Deserializer;
import com.syntifi.casper.sdk.jackson.serializer.CLValueU256Serializer;
import com.syntifi.casper.sdk.model.storedvalue.clvalue.encdec.CLValueDecoder;
import com.syntifi.casper.sdk.model.storedvalue.clvalue.encdec.CLValueEncoder;

/**
 * Casper U256 CLValue implementation
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @see AbstractCLValue
 * @since 0.0.1
 */
@JsonSerialize(using = CLValueU256Serializer.class)
@JsonDeserialize(using = CLValueU256Deserializer.class)
public class CLValueU256 extends AbstractCLValue<BigInteger> {

    public CLValueU256() {
        this(null);
    }

    public CLValueU256(BigInteger value) {
        super(value, new CLType(CLTypeData.U256, null));
    }

    @Override
    public void encode(CLValueEncoder clve) throws IOException, CLValueEncodeException {
        clve.writeU256(this);
    }

    @Override
    public void decode(CLValueDecoder clvd) throws IOException, CLValueDecodeException {
        clvd.readU256(this);
    }
}
