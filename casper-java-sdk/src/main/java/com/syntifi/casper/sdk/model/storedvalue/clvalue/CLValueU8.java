package com.syntifi.casper.sdk.model.storedvalue.clvalue;

import java.io.IOException;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.syntifi.casper.sdk.exception.CLValueDecodeException;
import com.syntifi.casper.sdk.jackson.deserializer.CLValueU8Deserializer;
import com.syntifi.casper.sdk.jackson.serializer.CLValueU8Serializer;
import com.syntifi.casper.sdk.model.storedvalue.clvalue.encdec.CLValueDecoder;
import com.syntifi.casper.sdk.model.storedvalue.clvalue.encdec.CLValueEncoder;

/**
 * Casper U8 CLValue implementation
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @see AbstractCLValue
 * @since 0.0.1
 */
@JsonSerialize(using = CLValueU8Serializer.class)
@JsonDeserialize(using = CLValueU8Deserializer.class)
public class CLValueU8 extends AbstractCLValue<Byte> {

    public CLValueU8() {
        this(null);
    }

    public CLValueU8(Byte value) {
        super(value, new CLType(CLTypeData.U8, null));
    }

    @Override
    public void encode(CLValueEncoder clve) throws IOException {
        clve.writeU8(this);
    }

    @Override
    public void decode(CLValueDecoder clvd) throws IOException, CLValueDecodeException {
        clvd.readU8(this);
    }
}
