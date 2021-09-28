package com.syntifi.casper.sdk.model.storedvalue.clvalue;

import java.io.IOException;
import java.util.Arrays;

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
        clvd.readByteArray(this);
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof CLValueByteArray)) return false;
        final CLValueByteArray other = (CLValueByteArray) o;
        if (!other.canEqual(this)) return false;
        if (this.getClType() == null ? other.getClType() != null : !this.getClType().equals(other.getClType())) return false;        
        if (this.getBytes() == null ?  other.getBytes() != null : !this.getBytes().equals( other.getBytes())) return false;
        if (this.getParsed() == null ? other.getParsed() != null : !this.getParsed().equals(other.getParsed())) return false;
        if (this.getValue() == null ? other.getValue() != null : !Arrays.equals(this.getValue(), other.getValue())) return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int result = 1;
        return result;
    }
}
