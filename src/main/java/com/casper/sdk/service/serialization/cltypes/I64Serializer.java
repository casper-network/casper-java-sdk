package com.casper.sdk.service.serialization.cltypes;

class I64Serializer extends FixedLengthNumberSerializer {

    public I64Serializer() {
        super(Long.BYTES, true);
    }
}
