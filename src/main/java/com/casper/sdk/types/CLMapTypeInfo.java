package com.casper.sdk.types;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The type info for a CLMap
 */
public class CLMapTypeInfo extends CLTypeInfo {

    /** The type of the maps keys */
    @JsonProperty("key")
    private final CLTypeInfo keyType;
    /** The type of the maps values */
    @JsonProperty("value")
    private final CLTypeInfo valueType;

    public CLMapTypeInfo(final CLTypeInfo keyType, final CLTypeInfo valueType) {
        super(CLType.MAP);
        this.keyType = keyType;
        this.valueType = valueType;
    }

    public CLTypeInfo getKeyType() {
        return keyType;
    }

    public CLTypeInfo getValueType() {
        return valueType;
    }
}
