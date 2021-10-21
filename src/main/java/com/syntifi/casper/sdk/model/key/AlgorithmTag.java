package com.syntifi.casper.sdk.model.key;

import java.security.NoSuchAlgorithmException;

import com.syntifi.casper.sdk.model.storedvalue.StoredValueData;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Algorithm byte tag
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @see StoredValueData
 * @since 0.0.1
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum AlgorithmTag implements Tag {
    SECP256K1((byte) 0x02), ED25519((byte) 0x01);

    private final byte byteTag;

    public static AlgorithmTag getByTag(byte byteTag) throws NoSuchAlgorithmException {
        for (AlgorithmTag a : values()) {
            if (a.byteTag == byteTag)
                return a;
        }
        throw new NoSuchAlgorithmException();
    }
}
