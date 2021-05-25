package com.casper.sdk.domain;

import com.casper.sdk.json.CLTypeInfoSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.Objects;

/**
 * The basic CL type info
 */
@JsonSerialize(using = CLTypeInfoSerializer.class)
public class CLTypeInfo {

    /** The type name */
    private final CLType type;

    public CLTypeInfo(CLType type) {
        this.type = type;
    }

    public CLType getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CLTypeInfo that = (CLTypeInfo) o;
        return type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type);
    }
}
