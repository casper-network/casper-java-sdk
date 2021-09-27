package com.syntifi.casper.sdk.model.storedvalue.clvalue;

import java.io.IOException;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.syntifi.casper.sdk.exception.CLValueDecodeException;
import com.syntifi.casper.sdk.jackson.deserializer.CLValueStringDeserializer;
import com.syntifi.casper.sdk.jackson.serializer.CLValueStringSerializer;
import com.syntifi.casper.sdk.model.storedvalue.clvalue.encdec.CLValueDecoder;
import com.syntifi.casper.sdk.model.storedvalue.clvalue.encdec.CLValueEncoder;

/**
 * Casper String CLValue implementation
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @see AbstractCLValue
 * @since 0.0.1
 */
@JsonSerialize(using = CLValueStringSerializer.class)
@JsonDeserialize(using = CLValueStringDeserializer.class)
public class CLValueString extends AbstractCLValue<String> {

    public CLValueString() {
        this(null);
    }

    public CLValueString(String value) {
        super(value, new CLType(CLTypeData.STRING, null));
    }

    @Override
    public void encode(CLValueEncoder clve) throws IOException {
        clve.writeString(this);
    }

    @Override
    public void decode(CLValueDecoder clvd) throws IOException, CLValueDecodeException {
        clvd.readString(this);
    }
}
