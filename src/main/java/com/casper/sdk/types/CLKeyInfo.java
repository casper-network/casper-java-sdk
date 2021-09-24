package com.casper.sdk.types;

public class CLKeyInfo extends CLTypeInfo {

    /**
     * The type of the key
     */
    public enum KeyType implements HasTag {

        /** The Account variant */
        ACCOUNT_ID(0),
        /** The Hash variant */
        HASH_ID(1),
        /** The URef variant */
        UREF_ID(2);

        private final int tag;

        KeyType(int tag) {
            this.tag = tag;
        }

        @Override
        public int getTag() {
            return tag;
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
