package com.casper.sdk.service.serialization.cltypes;

import com.casper.sdk.service.serialization.util.ByteUtils;
import com.casper.sdk.domain.CLType;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

class StringType implements TypesSerializer {

    private final TypesFactory typesFactory;

    public StringType(final TypesFactory typesFactory) {
        this.typesFactory = typesFactory;
    }

    @Override
    public byte[] serialize(final Object toSerialize) {

        final String str = toSerialize != null ? toSerialize.toString() : "";
        final ByteBuffer buffer = ByteBuffer.allocate(str.length()).put(str.getBytes(StandardCharsets.UTF_8));

        final byte[] bytes = buffer.array();

        //return the length of the string in U32 type plus the hex byte value of the string
        return ByteUtils.concat(
                typesFactory.getInstance(CLType.U32).serialize(str.length()),
                bytes
        );
    }
}