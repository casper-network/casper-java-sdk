package com.casper.sdk.types;

import com.casper.sdk.service.serialization.util.ByteUtils;

/**
 * Specialisation of a value for an CL Option type
 */
public class CLOptionValue extends CLValue {

    /** Option value prefix when no value is present for inner type */
    public static final byte[] OPTION_NONE = new byte[]{0x00};
    /** Option value prefix when value is present for inner type */
    public static final byte[] OPTION_SOME = new byte[]{0x01};

    /**
     * @param hexBytes the option bytes with the 1st byte being OPTION_NONE or OPTION_SOME
     * @param typeInfo the type information
     * @param parsed   the parsed value
     */
    public CLOptionValue(final String hexBytes, final CLOptionTypeInfo typeInfo, final Object parsed) {
        super(hexBytes, typeInfo, parsed);
    }

    /**
     * @param bytes    the option bytes with the 1st byte being OPTION_NONE or OPTION_SOME
     * @param typeInfo the type information
     * @param parsed   the parsed value
     */
    public CLOptionValue(final byte[] bytes, final CLOptionTypeInfo typeInfo, final Object parsed) {
        super(bytes, typeInfo, parsed);
    }

    public static byte[] prefixOption(final byte[] bytes) {

        final byte[] optionPrefix;
        if (bytes == null || bytes.length == 0) {
            optionPrefix = OPTION_NONE;
        } else {
            optionPrefix = OPTION_SOME;
        }
        return ByteUtils.concat(optionPrefix, bytes);
    }
}
