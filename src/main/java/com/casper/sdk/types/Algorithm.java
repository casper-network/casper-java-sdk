package com.casper.sdk.types;

import java.util.HashMap;
import java.util.Map;

/**
 * The enum of supported signature algorithms
 */
public enum Algorithm {

    ED25519(1),
    SECP256K1(2);

    private static final Map<Integer, Algorithm> map = new HashMap<>();

    static {
        for (Algorithm key : Algorithm.values()) {
            map.put(key.value, key);
        }
    }

    private final int value;

    Algorithm(int value) {
        this.value = value;
    }

    public static Algorithm fromId(char id) {
        if (id == 1 || id == '1') {
            return ED25519;
        } else if (id == 2 || id == '2') {
            return SECP256K1;
        }
        throw new IllegalArgumentException("Unknown algorithm ID " + id);
    }

    public static Algorithm fromString(final String algorithm) {
        switch (algorithm) {
            case "ECDSA":
                return SECP256K1;
            case "Ed25519":
                return ED25519;
        }
        throw new IllegalArgumentException("Unknown algorithm " + algorithm);
    }

    public int getValue() {
        return value;
    }
}

