package com.casper.sdk.domain;

/**
 * Specialised type for Options that have an inner type
 */
public class CLOptionTypeInfo extends CLTypeInfo {

    /** Option value prefix when no value is present for inner type */
    public static final byte[] OPTION_NONE = new byte[]{0x00};
    /** Option value prefix when value is present for inner type */
    public static final byte[] OPTION_SOME = new byte[]{0x01};

    private final CLTypeInfo innerType;

    public CLOptionTypeInfo(final CLTypeInfo innerType) {
        super(CLType.OPTION);
        this.innerType = innerType;
    }

    public CLTypeInfo getInnerType() {
        return innerType;
    }
}
