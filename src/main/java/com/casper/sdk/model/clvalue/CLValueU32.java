package com.casper.sdk.model.clvalue;

import com.casper.sdk.annotation.ExcludeFromJacocoGeneratedReport;
import com.casper.sdk.model.clvalue.cltype.CLTypeU32;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import dev.oak3.sbs4j.DeserializerBuffer;
import dev.oak3.sbs4j.SerializerBuffer;
import dev.oak3.sbs4j.exception.ValueSerializationException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bouncycastle.util.encoders.Hex;

/**
 * Casper U32 CLValue implementation
 *
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @see AbstractCLValue
 * @since 0.0.1
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class CLValueU32 extends AbstractCLValue<Long, CLTypeU32> {
    private CLTypeU32 clType = new CLTypeU32();

    @JsonSetter("cl_type")
    @ExcludeFromJacocoGeneratedReport
    protected void setJsonClType(final CLTypeU32 clType) {
        this.clType = clType;
    }

    @JsonGetter("cl_type")
    @ExcludeFromJacocoGeneratedReport
    protected String getJsonClType() {
        return this.getClType().getTypeName();
    }

    public CLValueU32(final Long value) throws ValueSerializationException {
        this.setValue(value);
    }

    @Override
    protected void serializeValue(final SerializerBuffer ser) throws ValueSerializationException {
        final SerializerBuffer serVal = new SerializerBuffer();
        serVal.writeU32(this.getValue());
        final byte[] bytes = serVal.toByteArray();
        ser.writeByteArray(bytes);
        this.setBytes(Hex.toHexString(bytes));
    }

    @Override
    public void deserializeCustom(final DeserializerBuffer deser) throws Exception {
        this.setValue(deser.readU32());
    }

    @Override
    public String toString() {
        return getValue() != null ? getValue().toString() : null;
    }
}
