package com.casper.sdk.service.serialization.cltypes;

class U256Serializer extends LargeNumberSerializer {

    private static final int MAX_BYTES = 32;

    U256Serializer() {
        super(MAX_BYTES);
    }
}
