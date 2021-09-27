package com.syntifi.casper.sdk.model.storedvalue.clvalue;

import java.io.IOException;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.syntifi.casper.sdk.exception.CLValueDecodeException;
import com.syntifi.casper.sdk.jackson.deserializer.CLValueBoolDeserializer;
import com.syntifi.casper.sdk.jackson.serializer.CLValueBoolSerializer;
import com.syntifi.casper.sdk.model.storedvalue.clvalue.encdec.CLValueDecoder;
import com.syntifi.casper.sdk.model.storedvalue.clvalue.encdec.CLValueEncoder;

/**
 * Casper Bool CLValue implementation
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @see AbstractCLValue
 * @since 0.0.1
 */
@JsonSerialize(using = CLValueBoolSerializer.class)
@JsonDeserialize(using = CLValueBoolDeserializer.class)
public class CLValueBool extends AbstractCLValue<Boolean> {

    public CLValueBool() {
        this(null);
    }

    public CLValueBool(Boolean value) {
        super(value, new CLType(CLTypeData.BOOL, null));
    }

    @Override
    public void encode(CLValueEncoder clve) throws IOException {
        clve.writeBool(this);
    }

    @Override
    public void decode(CLValueDecoder clvd) throws IOException, CLValueDecodeException {
        clvd.readBool(this);
    }
}
