package com.casper.sdk.model.key;

import com.casper.sdk.exception.NoSuchKeyTagException;
import com.casper.sdk.model.storedvalue.StoredValueData;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @see StoredValueData
 * @since 0.0.1
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum KeyTag implements Tag {
    ACCOUNT((byte) 0x00, Key.class),
    HASH((byte) 0x01, Key.class),
    UREF((byte) 0x02, Key.class),
    TRANSFER((byte) 0x03, Key.class),
    DEPLOY_INFO((byte) 0x04, Key.class),
    ERAI_NFO((byte) 0x05, Key.class),
    BALANCE((byte) 0x06, Key.class),
    BID((byte) 0x07, Key.class),
    WITHDRAW((byte) 0x08, Key.class),
    DICTIONARY((byte) 0x09, Key.class),
    SYSTEM_ENTITY_REGISTRY((byte) 0x0a, Key.class),
    ERA_SUMMARY((byte) 0x0b, Key.class),
    UNBOND((byte) 0x0c, Key.class),
    CHAINSPEC_REGISTRY((byte) 0x0d, Key.class),
    CHECKSUM_REGISTRY((byte) 0x0e, Key.class),
    BID_ADDR((byte) 0x0f, BidAddrKey.class),
    PACKAGE((byte) 0x10, Key.class),
    ADDRESSABLE_ENTITY((byte) 0x11, AddressableEntityKey.class),
    BYTE_CODE((byte) 0x12, ByteCodeKey.class),
    MESSAGE((byte) 0x13, Key.class),
    NAMED_KEY((byte) 0x14, Key.class),
    BLOCK_GLOBAL((byte) 0x15, Key.class),
    BALANCE_HOLD((byte) 0x16, Key.class),
    ENTRY_POINT((byte) 0x17, Key.class);

    private final byte byteTag;
    private final Class<? extends Key> keyClass;

    public static KeyTag getByTag(byte tag) throws NoSuchKeyTagException {
        for (KeyTag a : values()) {
            if (a.byteTag == tag)
                return a;
        }
        throw new NoSuchKeyTagException();
    }
}
