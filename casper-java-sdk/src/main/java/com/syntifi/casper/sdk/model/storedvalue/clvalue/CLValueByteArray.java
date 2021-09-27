package com.syntifi.casper.sdk.model.storedvalue.clvalue;

import java.io.IOException;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.syntifi.casper.sdk.exception.CLValueDecodeException;
import com.syntifi.casper.sdk.jackson.deserializer.CLValueByteArrayDeserializer;
import com.syntifi.casper.sdk.jackson.serializer.CLValueByteArraySerializer;
import com.syntifi.casper.sdk.model.storedvalue.clvalue.encdec.CLValueDecoder;
import com.syntifi.casper.sdk.model.storedvalue.clvalue.encdec.CLValueEncoder;

/**
 * Casper ByteArray CLValue implementation
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @see AbstractCLValue
 * @since 0.0.1
 */
@JsonSerialize(using = CLValueByteArraySerializer.class)
@JsonDeserialize(using = CLValueByteArrayDeserializer.class)
public class CLValueByteArray extends AbstractCLValue<byte[]> {

    public CLValueByteArray() {
        this(null);
    }

    public CLValueByteArray(byte[] value) {
        super(value, new CLType(CLTypeData.BYTE_ARRAY, null));
    }

    @Override
    public void encode(CLValueEncoder clve) throws IOException {
        clve.writeByteArray(this);
    }

    @Override
    public void decode(CLValueDecoder clvd) throws IOException, CLValueDecodeException {
        // FIXME: How to correctly use bytearray
        clvd.readByteArray(this, 32);
    }
}
