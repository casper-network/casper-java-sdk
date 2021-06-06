package com.casper.sdk.service;

import java.util.HashMap;
import java.util.Map;

/**
 * Account Key prefixes
 */
class TypeEnums {

    public enum KeyAlgorithm {

        ED25519(1),
        SECP256K1(2);

        private static final Map<Integer, KeyAlgorithm> map = new HashMap<>();

        static {
            for (KeyAlgorithm key : KeyAlgorithm.values()) {
                map.put(key.value, key);
            }
        }

        private final int value;

        KeyAlgorithm(int value) {
            this.value = value;
        }

        public static KeyAlgorithm valueOf(int pageType) {
            return (KeyAlgorithm) map.get(pageType);
        }

        public int getValue() {
            return value;
        }

    }

}
