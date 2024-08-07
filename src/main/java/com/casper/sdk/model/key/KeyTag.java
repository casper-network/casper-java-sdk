package com.casper.sdk.model.key;

import com.casper.sdk.exception.NoSuchKeyTagException;
import com.casper.sdk.model.storedvalue.StoredValueData;
import lombok.Getter;

import java.util.Collections;
import java.util.List;

import static java.util.Arrays.asList;

/**
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @see StoredValueData
 * @since 0.0.1
 */
@Getter
public enum KeyTag implements Tag {
    ACCOUNT((byte) 0x00, Key.class, "account-hash-"),
    HASH((byte) 0x01, Key.class, "hash-", "contract-"),
    // FIXME Make URef a key
    UREF((byte) 0x02, URefKey.class, "uref-"),
    TRANSFER((byte) 0x03, Key.class, "transfer-"),
    DEPLOY_INFO((byte) 0x04, Key.class, "deploy-"),
    ERA_INFO((byte) 0x05, EraInfoKey.class, "era-"),
    BALANCE((byte) 0x06, Key.class, "balance-"),
    BID((byte) 0x07, Key.class, "bid-"),
    WITHDRAW((byte) 0x08, Key.class, "withdraw-"),
    DICTIONARY((byte) 0x09, Key.class, "dictionary-"),
    SYSTEM_ENTITY_REGISTRY((byte) 0x0a, Key.class, "system-contract-registry-"),
    ERA_SUMMARY((byte) 0x0b, Key.class, "era-summary-"),
    UNBOND((byte) 0x0c, Key.class, "unbond-"),
    CHAINSPEC_REGISTRY((byte) 0x0d, Key.class, "chainspec-registry-"),
    CHECKSUM_REGISTRY((byte) 0x0e, Key.class, "checksum-registry-"),
    BID_ADDR((byte) 0x0f, BidAddrKey.class, "bid-addr-"),
    PACKAGE((byte) 0x10, Key.class, "package-"),
    ADDRESSABLE_ENTITY((byte) 0x11, AddressableEntityKey.class, "entity-"),
    BYTE_CODE((byte) 0x12, ByteCodeKey.class, "byte-code-"),
    MESSAGE((byte) 0x13, MessageKey.class, "message-"),
    NAMED_KEY((byte) 0x14, NamedKeyKey.class, "named-key-"),
    BLOCK_GLOBAL((byte) 0x15, BlockGlobalKey.class, "block-"),
    ENTRY_POINT((byte) 0x17, EntryPointKey.class, "entry-point-"),
    BALANCE_HOLD((byte) 0x16, BalanceHoldKey.class, "balance-hold-");

    private final byte byteTag;
    private final Class<? extends Key> keyClass;
    private final String[] keyNames;

    KeyTag(byte byteTag, Class<? extends Key> keyClass, String... keyNames) {
        this.byteTag = byteTag;
        this.keyClass = keyClass;
        this.keyNames = keyNames;
    }

    private static final List<KeyTag> reversed = reversed();

    public static KeyTag getByTag(final byte tag) throws NoSuchKeyTagException {
        for (final KeyTag a : values()) {
            if (a.byteTag == tag)
                return a;
        }
        throw new NoSuchKeyTagException("No such tag: " + tag);
    }

    public static KeyTag getByKeyName(final String keyName) throws NoSuchKeyTagException {
        // Search in reverse order to get the most specific key eg 'bid-addr-' and 'bid-'
        for (final KeyTag a : reversed) {
            for (final String name : a.keyNames) {
                if (keyName.startsWith(name)) {
                    return a;
                }
            }
        }
        throw new NoSuchKeyTagException("No such key name: " + keyName);
    }

    private static List<KeyTag> reversed() {
        final List<KeyTag> reversed = asList(values());
        Collections.reverse(reversed);
        return reversed;
    }

    public String getKeyName() {
        return keyNames[0];
    }
}
