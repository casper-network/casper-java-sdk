package com.casper.sdk.model.key;

import dev.oak3.sbs4j.DeserializerBuffer;
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
    protected void deserializeCustom(final DeserializerBuffer deser) throws Exception {
        setKey(deser.readByteArray(65));
        baseAddr = new AddressableEntityKey();
        baseAddr.deserializeCustom(new DeserializerBuffer(Arrays.copyOfRange(getKey(), 0, 33)));
        stringBytes = Arrays.copyOfRange(getKey(), 33, 65);
    }
}
