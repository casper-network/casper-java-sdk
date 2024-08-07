package com.casper.sdk.model.key;

import com.casper.sdk.model.storedvalue.StoredValueData;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.security.NoSuchAlgorithmException;

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

    SECP256K1((byte) 0x02, 33),
    ED25519((byte) 0x01, 32);

    private final byte byteTag;
    /** The number of bytes for a key excluding the tag byte */
    private final int length;

    public static AlgorithmTag getByTag(byte byteTag) throws NoSuchAlgorithmException {
        for (AlgorithmTag a : values()) {
            if (a.byteTag == byteTag)
                return a;
        }
        throw new NoSuchAlgorithmException();
    }

}
