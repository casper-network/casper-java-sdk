package com.casper.sdk.service.serialization.cltypes;

import com.casper.sdk.service.serialization.util.ByteUtils;

class U8Serializer implements TypesSerializer {
    @Override
    public byte[] serialize(final Object toSerialize) {

        if (toSerialize instanceof Byte)
            return new byte[]{(byte) toSerialize};
        else if (toSerialize instanceof Number) {
            if (((Number) toSerialize).shortValue() > 255) {
                throw new IllegalArgumentException("Not a valid byte " + toSerialize);
            }
            return serialize(((Number) toSerialize).byteValue());
        } else if (toSerialize instanceof String && ((String) toSerialize).length() == 2) {
            // assume a 2 character representation of a byte
            return ByteUtils.decodeHex((String) toSerialize);
        } else {
            throw new IllegalArgumentException("Not a valid byte " + toSerialize);
        }
    }
}
