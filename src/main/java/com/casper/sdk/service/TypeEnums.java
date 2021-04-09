package com.casper.sdk.service;

import java.util.HashMap;
import java.util.Map;

/**
 * Account Key prefixes
 */
class TypeEnums {

    public enum KeyAlgorithm {

        ED25519(01),
        SECP256K1(02);

        private final int value;
        private static final Map map = new HashMap<>();

        KeyAlgorithm(int value) {
            this.value = value;
        }

        static {
            for (KeyAlgorithm key : KeyAlgorithm.values()) {
                map.put(key.value, key);
            }
        }

        public static KeyAlgorithm valueOf(int pageType) {
            return (KeyAlgorithm) map.get(pageType);
        }

        public int getValue() {
            return value;
        }

    }

}
