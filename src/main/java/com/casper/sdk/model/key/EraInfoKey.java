package com.casper.sdk.model.key;

import dev.oak3.sbs4j.DeserializerBuffer;
import dev.oak3.sbs4j.SerializerBuffer;
import dev.oak3.sbs4j.exception.ValueSerializationException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;

/**
 * @author ian@meywood.com
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EraInfoKey extends Key {

    private BigInteger eraId;

    @Override
    protected void deserializeCustom(final DeserializerBuffer deser) throws Exception {
        eraId = deser.readU64();
        final SerializerBuffer ser = new SerializerBuffer();
        ser.writeU64(eraId);
        setKey(ser.toByteArray());
    }

    @Override
    protected void fromStringCustom(final String strKey) {
        try {
            final String[] split = strKey.split("-");
            eraId = new BigInteger(split[split.length - 1]);
            final SerializerBuffer ser = new SerializerBuffer();
            ser.writeU64(eraId);
            setKey(ser.toByteArray());
        } catch (ValueSerializationException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public String toString() {
        return this.getTag().getKeyName() + eraId;
    }
}
