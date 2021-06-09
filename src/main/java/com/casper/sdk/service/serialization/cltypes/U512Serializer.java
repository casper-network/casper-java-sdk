package com.casper.sdk.service.serialization.cltypes;

class U512Serializer extends LargeNumberSerializer {

    private static final int MAX_BYTES = 64;

    U512Serializer() {
        super(MAX_BYTES);
    }
}
