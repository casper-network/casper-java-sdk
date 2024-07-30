package com.casper.sdk.model.clvalue;

import com.casper.sdk.annotation.ExcludeFromJacocoGeneratedReport;
import com.casper.sdk.model.clvalue.cltype.CLTypePublicKey;
import com.casper.sdk.model.key.PublicKey;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import dev.oak3.sbs4j.DeserializerBuffer;
import dev.oak3.sbs4j.SerializerBuffer;
import dev.oak3.sbs4j.exception.ValueSerializationException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Casper PublicKey CLValue implementation
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
public class CLValuePublicKey extends AbstractCLValue<PublicKey, CLTypePublicKey> {
    private CLTypePublicKey clType = new CLTypePublicKey();

    @JsonSetter("cl_type")
    @ExcludeFromJacocoGeneratedReport
    protected void setJsonClType(final CLTypePublicKey clType) {
        this.clType = clType;
    }

    @JsonGetter("cl_type")
    @ExcludeFromJacocoGeneratedReport
    protected String getJsonClType() {
        return this.getClType().getTypeName();
    }

    public CLValuePublicKey(final PublicKey value) throws ValueSerializationException {
        this.setValue(value);
        this.setParsed(value.getAlgoTaggedHex());
    }

    @Override
    protected void serializeValue(final SerializerBuffer ser) throws ValueSerializationException {
        ser.writeU8(this.getValue().getTag().getByteTag());
        ser.writeByteArray(this.getValue().getKey());
        this.setBytes(this.getValue().getAlgoTaggedHex());
    }

    @Override
    public void deserializeCustom(final DeserializerBuffer deser) throws Exception {
        this.setValue(PublicKey.deserialize(deser));
    }

    @Override
    public String toString() {
        return getValue() != null ? getValue().getAlgoTaggedHex() : null;
    }
}
