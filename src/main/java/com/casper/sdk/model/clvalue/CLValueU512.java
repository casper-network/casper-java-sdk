package com.casper.sdk.model.clvalue;

import com.casper.sdk.annotation.ExcludeFromJacocoGeneratedReport;
import com.casper.sdk.model.clvalue.cltype.CLTypeU512;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import dev.oak3.sbs4j.DeserializerBuffer;
import dev.oak3.sbs4j.SerializerBuffer;
import dev.oak3.sbs4j.exception.ValueSerializationException;
import lombok.*;
import org.bouncycastle.util.encoders.Hex;

import java.math.BigInteger;

/**
 * Casper U512 CLValue implementation
 *
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @see AbstractCLValue
 * @since 0.0.1
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class CLValueU512 extends AbstractCLValue<BigInteger, CLTypeU512> {
    private CLTypeU512 clType = new CLTypeU512();

    @JsonSetter("cl_type")
    @ExcludeFromJacocoGeneratedReport
    protected void setJsonClType(final CLTypeU512 clType) {
        this.clType = clType;
    }

    @JsonGetter("cl_type")
    @ExcludeFromJacocoGeneratedReport
    protected String getJsonClType() {
        return this.getClType().getTypeName();
    }

    public CLValueU512(final BigInteger value) throws ValueSerializationException {
        this.setValue(value);
    }

    @Override
    protected void serializeValue(final SerializerBuffer ser) throws ValueSerializationException {
        final SerializerBuffer serVal = new SerializerBuffer();
        serVal.writeU512(this.getValue());
        final byte[] bytes = serVal.toByteArray();
        ser.writeByteArray(bytes);
        this.setBytes(Hex.toHexString(bytes));
    }

    @Override
    public void deserializeCustom(final DeserializerBuffer deser) throws Exception {
        this.setValue(deser.readU512());
    }

    @Override
    public String toString() {
        return getValue() != null ? getValue().toString() : null;
    }
}
