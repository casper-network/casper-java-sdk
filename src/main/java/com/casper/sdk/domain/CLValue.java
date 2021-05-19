package com.casper.sdk.domain;

/**
 * Domain type: a value to be interpreted by node software.
 */
public class CLValue extends AbstractCLType {
    /** Byte array representation of underlying data. */
    private final byte[] bytes;

    public CLValue(final String hexBytes, final CLType clType) {
        this(fromString(hexBytes), clType);
    }

    public CLValue(final byte[] bytes, final CLType clType) {
        super(clType);
        this.bytes = bytes;
    }

    public byte[] getBytes() {
        return bytes;
    }
}
