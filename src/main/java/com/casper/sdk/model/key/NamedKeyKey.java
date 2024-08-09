package com.casper.sdk.model.key;

import com.casper.sdk.exception.NoSuchKeyTagException;
import com.syntifi.crypto.key.encdec.Hex;
import dev.oak3.sbs4j.DeserializerBuffer;
import dev.oak3.sbs4j.SerializerBuffer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Arrays;

/**
 * The key for a named key.
 *
 * @author ian@meywood.com
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NamedKeyKey extends Key {

    /** The address of the entity. */
    private AddressableEntityKey baseAddr;
    /** The bytes of the name. */
    private byte[] stringBytes;

    @Override
    protected void fromStringCustom(final String strKey) {
        final String[] split = strKey.split("-");
        try {
            final String baseAddrStr = split[2] + "-" + split[3] + "-" + split[4];
            baseAddr = (AddressableEntityKey) Key.fromKeyString(baseAddrStr);
            stringBytes = Hex.decode(split[5]);

            final SerializerBuffer ser = new SerializerBuffer();
            ser.writeU8(baseAddr.getEntityAddressTag().getByteTag());
            ser.writeByteArray(baseAddr.getKey());
            ser.writeByteArray(stringBytes);
            setKey(ser.toByteArray());
        } catch (NoSuchKeyTagException e) {
            throw new IllegalArgumentException(e);
        }
    }



    @Override
    public String toString() {
        return this.getTag().getKeyName() +
                baseAddr + "-" +
                Hex.encode(stringBytes);
    }

    @Override
    protected void deserializeCustom(final DeserializerBuffer deser) throws Exception {
        setKey(deser.readByteArray(65));
        baseAddr = new AddressableEntityKey();
        baseAddr.deserializeCustom(new DeserializerBuffer(Arrays.copyOfRange(getKey(), 0, 33)));
        stringBytes = Arrays.copyOfRange(getKey(), 33, 65);
    }
}
