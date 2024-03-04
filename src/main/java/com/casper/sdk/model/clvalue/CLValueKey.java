package com.casper.sdk.model.clvalue;

import com.casper.sdk.annotation.ExcludeFromJacocoGeneratedReport;
import com.casper.sdk.model.clvalue.cltype.CLTypeKey;
import com.casper.sdk.model.key.Key;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import dev.oak3.sbs4j.DeserializerBuffer;
import dev.oak3.sbs4j.SerializerBuffer;
import dev.oak3.sbs4j.exception.ValueSerializationException;
import dev.oak3.sbs4j.util.ByteUtils;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Casper Key CLValue implementation
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
public class CLValueKey extends AbstractCLValue<Key, CLTypeKey> {
    private CLTypeKey clType = new CLTypeKey();

    @JsonSetter("cl_type")
    @ExcludeFromJacocoGeneratedReport
    protected void setJsonClType(final CLTypeKey clType) {
        this.clType = clType;
    }

    @JsonGetter("cl_type")
    @ExcludeFromJacocoGeneratedReport
    protected String getJsonClType() {
        return this.getClType().getTypeName();
    }

    public CLValueKey(final Key value) throws ValueSerializationException {
        this.setValue(value);
    }

    @Override
    protected void serializeValue(final SerializerBuffer ser) throws ValueSerializationException {
        ser.writeU8(this.getValue().getTag().getByteTag());
        ser.writeByteArray(this.getValue().getKey());
        this.setBytes(this.getValue().getAlgoTaggedHex());
    }

    @Override
    public void deserializeCustom(final DeserializerBuffer deser) throws Exception {
        this.setValue(Key.fromTaggedHexString(ByteUtils.encodeHexString(deser.readByteArray(33))));
    }

    @Override
    public String toString() {
        return getValue() != null ? getValue().toString() : null;
    }
}
