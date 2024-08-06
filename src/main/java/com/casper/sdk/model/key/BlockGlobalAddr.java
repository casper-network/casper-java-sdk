package com.casper.sdk.model.key;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Address for singleton values associated to specific block. These are values which are
 * calculated or set during the execution of a block such as the block timestamp, or the
 * total count of messages emitted during the execution of the block, and so on.
 *
 * @author ian@meywood.com
 */
@AllArgsConstructor
@Getter
public enum BlockGlobalAddr implements Tag {

    /** Block time variant */
    BLOCK_TIME((byte) 0),
    /** Message count variant. */
    MESSAGE_COUNT((byte) 1);

    private final byte byteTag;

    public static BlockGlobalAddr getByTag(final byte byteTag) {
        for (BlockGlobalAddr addr : values()) {
            if (addr.byteTag == byteTag)
                return addr;
        }
        throw new IllegalArgumentException("No such BlockGlobalAddr: " + byteTag);
    }
}
