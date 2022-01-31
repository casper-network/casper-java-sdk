package com.casper.sdk.types;

import com.casper.sdk.service.json.deserialize.CLValueJsonDeserializer;
import com.casper.sdk.service.json.serialize.CLValueJsonSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.Arrays;

/**
 * Type: a value to be interpreted by node software.
 */
@JsonDeserialize(using = CLValueJsonDeserializer.class)
@JsonSerialize(using = CLValueJsonSerializer.class)
public class CLValue extends AbstractCLType {

    /** Byte array representation of underlying data. */
    private final byte[] bytes;
    /** The optional parsed value of the bytes used when testing */
    private Object parsed;

    public CLValue(final String hexBytes, final CLTypeInfo clType, final Object parsed) {
        this(fromString(hexBytes), clType);
        this.parsed = parsed;
    }

    public CLValue(final byte[] bytes, final CLTypeInfo clType, final Object parsed) {
        super(clType);
        this.bytes = bytes;
        this.parsed = parsed;
    }

    public CLValue(byte[] bytes, final CLType clType, final Object parsed) {
        this(bytes, new CLTypeInfo(clType), parsed);
    }

    public CLValue(final byte[] bytes, final CLTypeInfo clType) {
        this(bytes, clType, null);
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

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final CLValue value = (CLValue) o;
        return Arrays.equals(bytes, value.bytes);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(bytes);
    }
}
