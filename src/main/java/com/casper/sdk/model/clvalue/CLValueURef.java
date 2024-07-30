package com.casper.sdk.model.clvalue;

import com.casper.sdk.annotation.ExcludeFromJacocoGeneratedReport;
import com.casper.sdk.model.clvalue.cltype.CLTypeURef;
import com.casper.sdk.model.uref.URef;
import com.casper.sdk.model.uref.URefAccessRight;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import dev.oak3.sbs4j.DeserializerBuffer;
import dev.oak3.sbs4j.SerializerBuffer;
import dev.oak3.sbs4j.exception.ValueSerializationException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bouncycastle.util.encoders.Hex;

import java.util.Objects;

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

    public CLValueURef(final URef value) throws ValueSerializationException {
        this.setValue(value);
        this.setParsed(value.getJsonURef());
    }

    @JsonGetter("cl_type")
    @ExcludeFromJacocoGeneratedReport
    protected String getJsonClType() {
        return this.getClType().getTypeName();
    }

    @JsonSetter("cl_type")
    @ExcludeFromJacocoGeneratedReport
    protected void setJsonClType(final CLTypeURef clType) {
        this.clType = clType;
    }

    @Override
    protected void serializeValue(final SerializerBuffer ser) throws ValueSerializationException {
        final SerializerBuffer serVal = new SerializerBuffer();
        final URef uref = this.getValue();
        final byte[] urefByte = new byte[uref.getAddress().length + 1];
        System.arraycopy(uref.getAddress(), 0, urefByte, 0, uref.getAddress().length);
        urefByte[32] = uref.getAccessRight().serializationTag;
        serVal.writeByteArray(urefByte);
        final byte[] bytes = serVal.toByteArray();
        ser.writeByteArray(bytes);
        this.setBytes(Hex.toHexString(bytes));
    }

    @Override
    public void deserializeCustom(final DeserializerBuffer deser) throws Exception {
        final URef uref = new URef();
        final CLValueByteArray clValueByteArray = new CLValueByteArray(new byte[32]);
        clValueByteArray.deserializeCustom(deser);
        uref.setAddress(clValueByteArray.getValue());
        final CLValueU8 serializationTag = new CLValueU8((byte) 0);
        serializationTag.deserializeCustom(deser);
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
        if (!Objects.equals(thisBytes, otherBytes))
            return false;
        final URef thisValue = this.getValue();
        final URef otherValue = other.getValue();
        if (!Objects.equals(thisValue, otherValue))
            return false;
        final Object thisClType = this.getClType();
        final Object otherClType = other.getClType();
        return Objects.equals(thisClType, otherClType);
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

    @Override
    public String toString() {
        return getValue() != null ? getValue().getJsonURef() : null;
    }
}
