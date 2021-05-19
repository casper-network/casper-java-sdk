package com.casper.sdk.service.serialization.factory;

import org.apache.commons.codec.binary.Hex;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class StringType implements TypesInterface {

    @Override
    public String serialize(final Object toSerialize) {
        return null;
    }

    @Override
    public String serialize(final String toSerialize, final TypesFactory typesFactory) {

        final ByteBuffer buffer = ByteBuffer.allocate(toSerialize.length())
                .put(toSerialize.getBytes(StandardCharsets.UTF_8));

        byte[] bytes = buffer.array();

        //return the length of the string in U32 type plus the hex byte value of the string
        return typesFactory.getInstance(TypesEnum.U32.name()).serialize(String.valueOf(toSerialize.length()))
                + Hex.encodeHexString(bytes);
    }
}
