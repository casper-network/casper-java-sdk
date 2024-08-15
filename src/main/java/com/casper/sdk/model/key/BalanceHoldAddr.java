package com.casper.sdk.model.key;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Balance hold address.
 *
 * @author ian@meywood.com
 */
@AllArgsConstructor
@Getter
public enum BalanceHoldAddr implements Tag {
    /** Gas hold variant. */
    GAS((byte) 0, "gas"),
    /** Processing variant. */
    PROCESSING((byte) 1, "processing"),;

    private final byte byteTag;
    private final String keyName;

    public static BalanceHoldAddr getByTag(final byte byteTag) {
        for (BalanceHoldAddr addr : values()) {
            if (addr.byteTag == byteTag)
                return addr;
        }
        throw new IllegalArgumentException("No such BalanceHoldAddr: " + byteTag);
    }

    public static BalanceHoldAddr getByKeyName(final String name) {
        for (BalanceHoldAddr addr : values()) {
            if (addr.keyName.equals(name)) {
                return addr;
            }
        }
        throw new IllegalArgumentException("No such BalanceHoldAddr: " + name);
    }
}
