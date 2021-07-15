package com.casper.sdk.service.serialization.cltypes;

import org.apache.commons.lang3.NotImplementedException;

public class PublicKeySerializer implements TypesSerializer {

    public PublicKeySerializer() {
    }

    @Override
    public byte[] serialize(final Object toSerialize) {
        throw new NotImplementedException("PublicKeySerializer");
    }
}
