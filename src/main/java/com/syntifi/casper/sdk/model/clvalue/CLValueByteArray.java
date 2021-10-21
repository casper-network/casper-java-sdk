package com.syntifi.casper.sdk.model.clvalue;

import java.io.IOException;
import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.syntifi.casper.sdk.annotation.ExcludeFromJacocoGeneratedReport;
import com.syntifi.casper.sdk.exception.CLValueDecodeException;
import com.syntifi.casper.sdk.model.clvalue.cltype.CLTypeByteArray;
import com.syntifi.casper.sdk.model.clvalue.encdec.CLValueDecoder;
import com.syntifi.casper.sdk.model.clvalue.encdec.CLValueEncoder;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Casper ByteArray CLValue implementation
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @see AbstractCLValue
 * @since 0.0.1
 */
@Getter
@Setter
@NoArgsConstructor
public class CLValueByteArray extends AbstractCLValue<byte[], CLTypeByteArray> {
    @JsonProperty("cl_type")
    private CLTypeByteArray clType = new CLTypeByteArray();

    public CLValueByteArray(byte[] value) {
        this.setValue(value);
        this.clType.setLength(value.length);
    }

    @Override
    public void encode(CLValueEncoder clve) throws IOException {
        clve.writeByteArray(this);
    }

    @Override
    public void decode(CLValueDecoder clvd) throws IOException, CLValueDecodeException {
        clvd.readByteArray(this, this.getClType().getLength());
    }

    @Override
    @ExcludeFromJacocoGeneratedReport
    public boolean equals(final Object o) {
        if (o == this)
            return true;
        if (!(o instanceof CLValueByteArray))
            return false;
        final CLValueByteArray other = (CLValueByteArray) o;
        if (!other.canEqual((Object) this))
            return false;
        final Object thisBytes = this.getBytes();
        final Object otherBytes = other.getBytes();
        if (thisBytes == null ? otherBytes != null : !thisBytes.equals(otherBytes))
            return false;
        final byte[] thisValue = this.getValue();
        final byte[] otherValue = other.getValue();
        if (thisValue == null ? otherValue != null : !Arrays.equals(thisValue, otherValue))
            return false;
        final Object thisClType = this.getClType();
        final Object otherClType = other.getClType();
        if (thisClType == null ? otherClType != null : !thisClType.equals(otherClType))
            return false;
        return true;
    }

    @ExcludeFromJacocoGeneratedReport
    @Override
    protected boolean canEqual(final Object other) {
        return other instanceof CLValueByteArray;
    }

    @Override
    @ExcludeFromJacocoGeneratedReport
    public int hashCode() {
        final int PRIME = 59;
        int result = super.hashCode();
        final Object thisClType = this.getClType();
        result = result * PRIME + (thisClType == null ? 43 : thisClType.hashCode());
        return result;
    }
}
