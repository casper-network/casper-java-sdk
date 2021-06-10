package com.casper.sdk.service.serialization.cltypes;

class I32Serializer extends FixedLengthNumberSerializer {

    public I32Serializer() {
        super(Integer.BYTES, true);
    }
}
