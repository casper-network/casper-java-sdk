package com.casper.sdk.service.serialization.cltypes;

import org.apache.commons.lang3.BooleanUtils;

/**
 * Converts a boolean value or string representation of a boolean to a byte array where 1 represents  true and 0 false.
 */
class BoolSerializer implements TypesSerializer {

    @Override
    public byte[] serialize(final Object toSerialize) {

        final boolean value;

        if (toSerialize instanceof Boolean) {
            value = (boolean) toSerialize;
        } else if (toSerialize instanceof String) {
            value = BooleanUtils.toBoolean((String) toSerialize);
        } else if (toSerialize instanceof Number) {
            value = BooleanUtils.toBoolean(((Number) toSerialize).intValue());
        } else {
            value = false;
        }

        return new byte[]{(byte) (value ? 1 : 0)};
    }
}
