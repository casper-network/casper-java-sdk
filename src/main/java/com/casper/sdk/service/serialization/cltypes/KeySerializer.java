package com.casper.sdk.service.serialization.cltypes;

import com.casper.sdk.types.CLKeyValue;

class KeySerializer implements TypesSerializer {
    @Override
    public byte[] serialize(final Object toSerialize) {

        if (toSerialize instanceof CLKeyValue) {
            return ((CLKeyValue) toSerialize).getBytes();
        } else {
            return new byte[0];
        }
    }
}
