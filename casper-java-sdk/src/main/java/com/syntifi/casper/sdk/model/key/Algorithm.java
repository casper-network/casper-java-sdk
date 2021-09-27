package com.syntifi.casper.sdk.model.key;

import java.security.NoSuchAlgorithmException;

public enum Algorithm {
    SECP256K1((byte)0x02), ED25519((byte)0x01);

    private final byte tag;

    private Algorithm(byte tag) {
        this.tag = tag;
    }

    public byte getTag() {
        return this.tag;
    }

    public static Algorithm getByTag(byte tag) throws NoSuchAlgorithmException {
        for (Algorithm a : values()) {
            if (a.tag == tag)
                return a;
        }
        throw new NoSuchAlgorithmException();
    }

}
