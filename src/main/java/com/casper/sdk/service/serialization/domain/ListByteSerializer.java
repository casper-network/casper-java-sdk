package com.casper.sdk.service.serialization.domain;

import com.casper.sdk.service.serialization.util.ByteArrayBuilder;

import java.util.List;

import static com.casper.sdk.service.serialization.util.ByteUtils.toU32;

/**
 * The byte serializer for Lists of casper domain objects.
 */
public class ListByteSerializer implements ByteSerializer<List<?>> {

    private final ByteSerializerFactory factory;

    public ListByteSerializer(final ByteSerializerFactory factory) {
        this.factory = factory;
    }

    @Override
    public byte[] toBytes(List<?> source) {

        final ByteArrayBuilder builder = new ByteArrayBuilder();

        // Write the size of the list as the 1st 4 bytes
        builder.append(toU32(source.size()));

        // Write the list contents
        source.forEach(item -> builder.append(factory.getByteSerializer(item).toBytes(item)));

        return builder.toByteArray();
    }

    @Override
    public Class<List<?>> getType() {
        //noinspection unchecked,rawtypes
        return (Class) List.class;
    }
}
