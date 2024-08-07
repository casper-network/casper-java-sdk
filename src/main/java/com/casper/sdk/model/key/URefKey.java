package com.casper.sdk.model.key;

import com.casper.sdk.model.uref.URef;
import com.casper.sdk.model.uref.URefAccessRight;
import dev.oak3.sbs4j.DeserializerBuffer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Arrays;

/**
 * @author ian@meywood.com
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class URefKey extends Key {

    private URef uRef;

    @Override
    protected void deserializeCustom(final DeserializerBuffer deser) throws Exception {
        final byte[] bytes = deser.readByteArray(33);
        final URefAccessRight access = URefAccessRight.getTypeBySerializationTag(bytes[32]);
        this.uRef = new URef(Arrays.copyOfRange(bytes, 0, 32), access);
        setKey(bytes);
    }

    @Override
    protected void fromStringCustom(final String strKey) {

        try {
            uRef = URef.fromString(strKey);
            final byte[] key = new byte[33];
            System.arraycopy(uRef.getAddress(), 0, key, 0, 32);
            key[32] = uRef.getAccessRight().getSerializationTag();
            setKey(key);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error deserializing URefKey: " + strKey, e);
        }
    }


    @Override
    public String toString() {
        return uRef.getJsonURef();
    }
}
