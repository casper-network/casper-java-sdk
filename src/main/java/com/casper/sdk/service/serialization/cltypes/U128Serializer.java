package com.casper.sdk.service.serialization.cltypes;

class U128Serializer extends LargeNumberSerializer {

    private static final int MAX_BYTES = 16;

    U128Serializer() {
        super(MAX_BYTES);
    }
}
