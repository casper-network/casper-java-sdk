package com.casper.sdk.domain;

import com.casper.sdk.json.CLValueJsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * Domain type: a value to be interpreted by node software.
 */
@JsonDeserialize(using = CLValueJsonDeserializer.class)
public class CLValue extends AbstractCLType {

    /** Byte array representation of underlying data. */
    private final byte[] bytes;
    /** The optional parsed value of the bytes used when testing */
    private Object parsed;

    public CLValue(final String hexBytes, final CLTypeInfo clType, final Object parsed) {
        this(fromString(hexBytes), clType);
        this.parsed = parsed;
    }

    public CLValue(final byte[] bytes, final CLTypeInfo clType) {
        super(clType);
        this.bytes = bytes;
    }

    public CLValue(final byte[] bytes, final CLType clType) {
        super(new CLTypeInfo(clType));
        this.bytes = bytes;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public Object getParsed() {
        return parsed;
    }
}
