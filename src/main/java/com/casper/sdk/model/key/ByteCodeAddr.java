package com.casper.sdk.model.key;

import com.casper.sdk.exception.NoSuchKeyTagException;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * An address for ByteCode records stored in global state.
 *
 * @author ian@meywood.com
 */
@AllArgsConstructor
@Getter
public enum ByteCodeAddr {

    /** An address for byte code to be executed against the V1 Casper execution engine. */
    V1_CASPER_WASM((byte) 0, "v1-wasm"),
    /** An empty byte code record */
    EMPTY((byte) 1, "empty");

    private final byte byteTag;
    private final String keyName;

    public static ByteCodeAddr getByTag(byte tag) throws NoSuchKeyTagException {
        for (ByteCodeAddr a : values()) {
            if (a.byteTag == tag)
                return a;
        }
        throw new NoSuchKeyTagException();
    }

    public static ByteCodeAddr getByKeyName(String keyName) {
        for (ByteCodeAddr entityAddr: values()) {
            if (keyName.contains(entityAddr.keyName)) {
                return entityAddr;
            }
        }
       throw new IllegalArgumentException("No such key name: " + keyName);
    }
}
