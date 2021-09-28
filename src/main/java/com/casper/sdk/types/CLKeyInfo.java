package com.casper.sdk.types;

public class CLKeyInfo extends CLTypeInfo {

    /**
     * The type of the key
     */
    public enum KeyType implements HasTag {

        /** The Account variant */
        ACCOUNT_ID(0, "account-hash"),
        /** The Hash variant */
        HASH_ID(1, "hash"),
        /** The URef variant */
        UREF_ID(2, "uref");

        private final int tag;
        private final String parsedName;

        KeyType(final int tag, final String parsedName) {
            this.tag = tag;
            this.parsedName = parsedName;
        }

        public static KeyType valueOf(byte tag) {
            for (KeyType keyType : KeyType.values()) {
                if (tag == keyType.tag) {
                    return keyType;
                }
            }
            throw new IllegalArgumentException("Invalid key type: " + tag);
        }

        @Override
        public int getTag() {
            return tag;
        }

        public String getParsedName() {
            return parsedName;
        }
    }

    private final KeyType keyType;

    public CLKeyInfo(final KeyType keyType) {
        super(CLType.KEY);
        this.keyType = keyType;
    }

    public KeyType getKeyType() {
        return keyType;
    }
}
