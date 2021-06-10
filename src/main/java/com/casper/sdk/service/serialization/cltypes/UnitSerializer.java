package com.casper.sdk.service.serialization.cltypes;

import org.apache.commons.lang3.NotImplementedException;

class UnitSerializer implements TypesSerializer {
    @Override public byte[] serialize(final Object toSerialize) {
        throw new NotImplementedException("UnitSerializer");
    }
}
