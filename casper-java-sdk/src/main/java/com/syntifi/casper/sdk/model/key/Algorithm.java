package com.syntifi.casper.sdk.model.key;

import java.security.NoSuchAlgorithmException;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @see StoredValue
 * @since 0.0.1
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum Algorithm {
    SECP256K1((byte) 0x02), ED25519((byte) 0x01);

    private final byte tag;

    public static Algorithm getByTag(byte tag) throws NoSuchAlgorithmException {
        for (Algorithm a : values()) {
            if (a.tag == tag)
                return a;
        }
        throw new NoSuchAlgorithmException();
    }

}
