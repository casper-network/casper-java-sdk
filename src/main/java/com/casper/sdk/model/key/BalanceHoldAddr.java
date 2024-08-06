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
    GAS((byte) 0),
    /** Processing variant. */
    PROCESSING((byte) 1);

    private final byte byteTag;

    public static BalanceHoldAddr getByTag(final byte byteTag) {
        for (BalanceHoldAddr addr : values()) {
            if (addr.byteTag == byteTag)
                return addr;
        }
        throw new IllegalArgumentException("No such BalanceHoldAddr: " + byteTag);
    }
}
