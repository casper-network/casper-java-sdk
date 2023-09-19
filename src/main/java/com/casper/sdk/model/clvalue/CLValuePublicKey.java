package com.casper.sdk.model.clvalue;

import com.casper.sdk.annotation.ExcludeFromJacocoGeneratedReport;
import com.casper.sdk.exception.NoSuchTypeException;
import com.casper.sdk.model.clvalue.cltype.CLTypePublicKey;
import com.casper.sdk.model.clvalue.serde.Target;
import com.casper.sdk.model.key.PublicKey;
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
import org.bouncycastle.util.encoders.Hex;

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
    protected void setJsonClType(CLTypePublicKey clType) {
        this.clType = clType;
    }

    @JsonGetter("cl_type")
    @ExcludeFromJacocoGeneratedReport
    protected String getJsonClType() {
        return this.getClType().getTypeName();
    }

    public CLValuePublicKey(PublicKey value) throws ValueSerializationException {
        this.setValue(value);
    }

    @Override
    public void serialize(SerializerBuffer ser, Target target) throws NoSuchTypeException, ValueSerializationException {
        if (this.getValue() == null) return;

        if (target.equals(Target.BYTE)) {
            super.serializePrefixWithLength(ser);
        }

        ser.writeU8(this.getValue().getTag().getByteTag());
        ser.writeByteArray(this.getValue().getKey());

        if (target.equals(Target.BYTE)) {
            this.encodeType(ser);
        }

        this.setBytes(Hex.toHexString(ser.toByteArray()));
    }

    @Override
    public void deserializeCustom(DeserializerBuffer deser) throws Exception {
        switch (deser.readByteArray(1)[0]){
            case 0x1:
                this.setValue(PublicKey.fromTaggedHexString(ByteUtils.encodeHexString(deser.readByteArray(33))));
                break;
            case 0x2:
                this.setValue(PublicKey.fromTaggedHexString(ByteUtils.encodeHexString(deser.readByteArray(34))));
                break;
            default:
                throw new ValueSerializationException("Public key is not of known type!");
        }
    }

    @Override
    public String toString() {
        return getValue() != null ? getValue().getAlgoTaggedHex() : null;
    }
}
