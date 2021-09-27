package com.syntifi.casper.sdk.model.storedvalue.clvalue;

import java.io.IOException;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.syntifi.casper.sdk.exception.CLValueDecodeException;
import com.syntifi.casper.sdk.jackson.deserializer.CLValueU32Deserializer;
import com.syntifi.casper.sdk.jackson.serializer.CLValueU32Serializer;
import com.syntifi.casper.sdk.model.storedvalue.clvalue.encdec.CLValueDecoder;
import com.syntifi.casper.sdk.model.storedvalue.clvalue.encdec.CLValueEncoder;

/**
 * Casper U32 CLValue implementation
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @see AbstractCLValue
 * @since 0.0.1
 */
@JsonSerialize(using = CLValueU32Serializer.class)
@JsonDeserialize(using = CLValueU32Deserializer.class)
public class CLValueU32 extends AbstractCLValue<Long> {

    public CLValueU32() {
        this(null);
    }

    public CLValueU32(Long value) {
        super(value, new CLType(CLTypeData.U32, null));
    }

    @Override
    public void encode(CLValueEncoder clve) throws IOException {
        clve.writeU32(this);
    }

    @Override
    public void decode(CLValueDecoder clvd) throws IOException, CLValueDecodeException {
        clvd.readU32(this);
    }
}
