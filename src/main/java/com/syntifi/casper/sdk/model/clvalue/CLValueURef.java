package com.syntifi.casper.sdk.model.clvalue;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.syntifi.casper.sdk.annotation.ExcludeFromJacocoGeneratedReport;
import com.syntifi.casper.sdk.exception.CLValueDecodeException;
import com.syntifi.casper.sdk.exception.DynamicInstanceException;
import com.syntifi.casper.sdk.model.clvalue.cltype.CLTypeURef;
import com.syntifi.casper.sdk.model.clvalue.encdec.CLValueDecoder;
import com.syntifi.casper.sdk.model.clvalue.encdec.CLValueEncoder;
import com.syntifi.casper.sdk.model.clvalue.encdec.StringByteHelper;
import com.syntifi.casper.sdk.model.uref.URef;
import com.syntifi.casper.sdk.model.uref.URefAccessRight;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Casper Boolean CLURef implementation URef is a tuple that contains the
 * address of the URef and the access rights to that URef. The serialized
 * representation of the URef is 33 bytes long. The first 32 bytes are the byte
 * representation of the URef address, and the last byte contains the bits
 * corresponding to the access rights of the URef.
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @see AbstractCLValue
 * @see URef
 * @since 0.0.1
 */
@Getter
@Setter
@NoArgsConstructor
public class CLValueURef extends AbstractCLValue<URef, CLTypeURef> {
    private CLTypeURef clType = new CLTypeURef();

    @JsonSetter("cl_type")
    @ExcludeFromJacocoGeneratedReport
	protected void setJsonClType(CLTypeURef clType) {
        this.clType = clType;
    }

    @JsonGetter("cl_type")
    @ExcludeFromJacocoGeneratedReport
	protected String getJsonClType() {
        return this.getClType().getTypeName();
    }

    public CLValueURef(URef value) {
        this.setValue(value);
    }

    @Override
    public void encode(CLValueEncoder clve) throws IOException {
        URef uref = this.getValue();
        byte[] urefByte = new byte[uref.getAddress().length + 1];
        System.arraycopy(uref.getAddress(), 0, urefByte, 0, uref.getAddress().length);
        urefByte[32] = uref.getAccessRight().serializationTag; 
        setBytes(StringByteHelper.convertBytesToHex(urefByte));
    }

    @Override
    public void decode(CLValueDecoder clvd) throws IOException, CLValueDecodeException, DynamicInstanceException {
        URef uref = new URef();
        CLValueByteArray clValueByteArray = new CLValueByteArray(new byte[32]);
        clValueByteArray.decode(clvd);
        uref.setAddress(clValueByteArray.getValue());
        CLValueU8 serializationTag = new CLValueU8((byte) 0);
        serializationTag.decode(clvd);
        uref.setAccessRight(URefAccessRight.getTypeBySerializationTag(serializationTag.getValue()));
        setValue(uref);
    }

    @ExcludeFromJacocoGeneratedReport
    @Override
    public boolean equals(final Object o) {
        if (o == this)
            return true;
        if (!(o instanceof CLValueURef))
            return false;
        final CLValueURef other = (CLValueURef) o;
        if (!other.canEqual(this))
            return false;
        final Object thisBytes = this.getBytes();
        final Object otherBytes = other.getBytes();
        if (thisBytes == null ? otherBytes != null : !thisBytes.equals(otherBytes))
            return false;
        final URef thisValue = this.getValue();
        final URef otherValue = other.getValue();
        if (thisValue == null ? otherValue != null : !(thisValue.equals(otherValue)))
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
        return other instanceof CLValueURef;
    }

    @ExcludeFromJacocoGeneratedReport
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = super.hashCode();
        final Object thisClType = this.getClType();
        result = result * PRIME + (thisClType == null ? 43 : thisClType.hashCode());
        return result;
    }
}
