package com.syntifi.casper.sdk.model.key;

import com.syntifi.casper.sdk.exception.NoSuchKeyTagException;
import com.syntifi.casper.sdk.model.storedvalue.StoredValueData;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @see StoredValueData
 * @since 0.0.1
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum KeyTag implements Tag {
    ACCOUNT((byte) 0x00), HASH((byte) 0x01), UREF((byte) 0x02), TRANSFER((byte) 0x03), DEPLOYINFO((byte) 0x04),
    ERAINFO((byte) 0x05), BALANCE((byte) 0x06), BID((byte) 0x07), WITHDRAW((byte) 0x08);

    private final byte byteTag;

    public static KeyTag getByTag(byte tag) throws NoSuchKeyTagException {
        for (KeyTag a : values()) {
            if (a.byteTag == tag)
                return a;
        }
        throw new NoSuchKeyTagException();
    }
}
