package com.casper.sdk.types;

/**
 * Specialised type for Options that have an inner type
 */
public class CLOptionTypeInfo extends CLTypeInfo {

    private final CLTypeInfo innerType;

    public CLOptionTypeInfo(final CLTypeInfo innerType) {
        super(CLType.OPTION);
        this.innerType = innerType;
    }

    public CLTypeInfo getInnerType() {
        return innerType;
    }
}
