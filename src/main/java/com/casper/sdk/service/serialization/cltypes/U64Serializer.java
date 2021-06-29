package com.casper.sdk.service.serialization.cltypes;

class U64Serializer extends FixedLengthNumberSerializer {

    public U64Serializer() {
        super(Long.BYTES, false);
    }
}
