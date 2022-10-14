package com.casper.sdk.model.clvalue;

import com.casper.sdk.annotation.ExcludeFromJacocoGeneratedReport;
import com.casper.sdk.exception.NoSuchTypeException;
import com.casper.sdk.model.clvalue.cltype.CLTypeByteArray;
import com.casper.sdk.model.clvalue.serde.Target;
import com.fasterxml.jackson.annotation.JsonProperty;
import dev.oak3.sbs4j.DeserializerBuffer;
import dev.oak3.sbs4j.SerializerBuffer;
import dev.oak3.sbs4j.exception.ValueSerializationException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bouncycastle.util.encoders.Hex;

import java.util.Arrays;
import java.util.Objects;

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

    public CLValueByteArray(byte[] value) throws ValueSerializationException {
        this.setValue(value);
        this.clType.setLength(value.length);
    }

    @Override
    public void serialize(SerializerBuffer ser, Target target) throws NoSuchTypeException, ValueSerializationException {
        if (this.getValue() == null) return;

        if (target.equals(Target.BYTE)) {
            super.serializePrefixWithLength(ser);
        }

        ser.writeByteArray(this.getValue());

        if (target.equals(Target.BYTE)) {
            this.encodeType(ser);
        }

        this.setBytes(Hex.toHexString(ser.toByteArray()));
    }

    @Override
    public void deserializeCustom(DeserializerBuffer deser) throws Exception {
        this.setValue(deser.readByteArray(this.getClType().getLength()));
    }

    @Override
    @ExcludeFromJacocoGeneratedReport
    public boolean equals(final Object o) {
        if (o == this)
            return true;
        if (!(o instanceof CLValueByteArray))
            return false;
        final CLValueByteArray other = (CLValueByteArray) o;
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
