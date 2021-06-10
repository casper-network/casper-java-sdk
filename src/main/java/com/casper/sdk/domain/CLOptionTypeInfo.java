package com.casper.sdk.domain;

/**
 * Specialised type for Options that have an inner type
 */
public class CLOptionTypeInfo extends CLTypeInfo {

    private final CLTypeInfo innerType;

    public CLOptionTypeInfo(CLTypeInfo innerType) {
        super(CLType.OPTION);
        this.innerType = innerType;
    }

    public CLTypeInfo getInnerType() {
        return innerType;
    }
}
