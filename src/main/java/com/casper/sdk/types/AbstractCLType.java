package com.casper.sdk.types;

import com.casper.sdk.service.serialization.util.ByteUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

/**
 * Abstract base class for a CL type object that is stored/transferred as a byte array.
 */
abstract class AbstractCLType {

    /** The type of the value */
    @JsonProperty("cl_type")
    private final CLTypeInfo typeInfo;

    protected AbstractCLType(final CLTypeInfo typeInfo) {
        Objects.requireNonNull(typeInfo, "type cannot be null");
        this.typeInfo = typeInfo;
    }

    public static byte[] fromString(final String hex) {
        return hex != null ? ByteUtils.decodeHex(hex) : null;
    }

    public static String toHex(final byte[] bytes) {
        return ByteUtils.encodeHexString(bytes);
    }

    @JsonProperty("cl_type")
    public CLTypeInfo getCLTypeInfo() {
        return typeInfo;
    }

    @JsonIgnore
    public CLType getCLType() {
        return typeInfo.getType();
    }

    public String toHex() {
        return toHex(getBytes());
    }

    public abstract byte[] getBytes();
}
