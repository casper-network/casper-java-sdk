package com.casper.sdk.model.clvalue;

import com.casper.sdk.annotation.ExcludeFromJacocoGeneratedReport;
import com.casper.sdk.model.clvalue.cltype.CLTypeAny;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import dev.oak3.sbs4j.DeserializerBuffer;
import dev.oak3.sbs4j.SerializerBuffer;
import dev.oak3.sbs4j.exception.ValueSerializationException;
import dev.oak3.sbs4j.util.ByteUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bouncycastle.util.encoders.Hex;

import java.util.Arrays;
import java.util.Objects;

/**
 * Casper Object CLValue implementation
 *
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @see AbstractCLValue
 * @since 0.0.1
 */
@Getter
@Setter
@NoArgsConstructor
public class CLValueAny extends AbstractCLValue<byte[], CLTypeAny> {
    private CLTypeAny clType = new CLTypeAny();

    @JsonSetter("cl_type")
    @ExcludeFromJacocoGeneratedReport
    protected void setJsonClType(final CLTypeAny clType) {
        this.clType = clType;
    }

    @JsonGetter("cl_type")
    @ExcludeFromJacocoGeneratedReport
    protected String getJsonClType() {
        return this.getClType().getTypeName();
    }

    public CLValueAny(final byte[] value) throws ValueSerializationException {
        this.setValue(value);
    }

    @Override
    protected void serializeValue(final SerializerBuffer ser) throws ValueSerializationException {
        ser.writeByteArray(this.getValue());
        this.setBytes(Hex.toHexString(this.getValue()));
    }

    @Override
    public void deserializeCustom(final DeserializerBuffer deser) throws Exception {
        this.setValue(deser.readByteArray(deser.getBuffer().remaining()));
    }

    @Override
    @ExcludeFromJacocoGeneratedReport
    public boolean equals(final Object o) {
        if (o == this)
            return true;
        if (!(o instanceof CLValueAny))
            return false;
        final CLValueAny other = (CLValueAny) o;
        if (!other.canEqual(this))
            return false;
        final Object thisBytes = this.getBytes();
        final Object otherBytes = other.getBytes();
        if (!Objects.equals(thisBytes, otherBytes))
            return false;
        final byte[] thisValue = this.getValue();
        final byte[] otherValue = other.getValue();
        if (thisValue == null ? otherValue != null : !Arrays.equals(thisValue, otherValue))
            return false;
        final Object thisClType = this.getClType();
        final Object otherClType = other.getClType();
        return Objects.equals(thisClType, otherClType);
    }

    @ExcludeFromJacocoGeneratedReport
    @Override
    protected boolean canEqual(final Object other) {
        return other instanceof CLValueAny;
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

    @Override
    public String toString() {
        return getValue() != null ? ByteUtils.encodeHexString(getValue()) : null;
    }
}
