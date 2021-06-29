package com.casper.sdk.service.serialization.cltypes;

/**
 * Converts a value to a little endian 32 bit array
 */
class U32Serializer extends FixedLengthNumberSerializer {

    public U32Serializer() {
        super(Integer.BYTES, false);
    }
}
