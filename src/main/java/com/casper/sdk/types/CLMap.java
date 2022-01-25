package com.casper.sdk.types;

import java.util.Map;

/**
 * serializes as a list of key-value tuples. There must be a well-defined ordering on the keys, and in the
 * serialization, the pairs are listed in ascending order. This is done to ensure determinism in the serialization, as
 * Map data structures can be unordered.
 */
public class CLMap extends CLValue {

    private CLValue key;
    private CLValue value;

    public CLMap(final byte[] bytes, final Object parsed) {
        super(bytes, CLType.MAP, parsed);
    }

    public CLMap(final CLValue key, final CLValue value) {
        super(null, CLType.MAP, null);
        this.key = key;
        this.value = value;
    }



}
