package com.casper.sdk.service.serialization.factory;

import com.casper.sdk.domain.CLType;
import org.apache.commons.codec.binary.Hex;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

class StringType implements TypesSerializer {

    private final TypesFactory typesFactory;

    public StringType(final TypesFactory typesFactory) {
        this.typesFactory = typesFactory;
    }

    @Override
    public String serialize(final Object toSerialize) {

        final String str = toSerialize.toString();

        final ByteBuffer buffer = ByteBuffer.allocate(str.length())
                .put(str.getBytes(StandardCharsets.UTF_8));

        final byte[] bytes = buffer.array();

        //return the length of the string in U32 type plus the hex byte value of the string
        return typesFactory.getInstance(CLType.U32).serialize(String.valueOf(str.length()))
                + Hex.encodeHexString(bytes);
    }
}
