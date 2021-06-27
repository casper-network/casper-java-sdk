package com.casper.sdk.service.serialization.cltypes;

import com.casper.sdk.service.serialization.util.ByteUtils;

import java.nio.charset.StandardCharsets;

class StringSerializer extends AbstractTypesSerializer {

    public StringSerializer(final TypesFactory typesFactory) {
        super(typesFactory);
    }

    @Override
    public byte[] serialize(final Object toSerialize) {

        final String str = toSerialize != null ? toSerialize.toString() : "";
        byte[] bytes = str.getBytes(StandardCharsets.UTF_8);

        //return the length of the string in U32 type plus the hex byte value of the string
        return ByteUtils.concat(
                getU32Serializer().serialize(bytes.length),
                bytes
        );
    }
}