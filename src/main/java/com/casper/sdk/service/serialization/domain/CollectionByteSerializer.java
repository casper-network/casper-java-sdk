package com.casper.sdk.service.serialization.domain;

import com.casper.sdk.service.serialization.util.ByteArrayBuilder;

import java.util.Collection;

import static com.casper.sdk.service.serialization.util.ByteUtils.toU32;

/**
 * The byte serializer for Lists of casper domain objects.
 */
public class CollectionByteSerializer implements ByteSerializer<Collection<?>> {

    private final ByteSerializerFactory factory;

    public CollectionByteSerializer(final ByteSerializerFactory factory) {
        this.factory = factory;
    }

    @Override
    public byte[] toBytes(Collection<?> source) {

        final ByteArrayBuilder builder = new ByteArrayBuilder();

        // Write the size of the list as the 1st 4 bytes
        builder.append(toU32(source.size()));

        // Write the list contents
        source.forEach(item ->
                builder.append(
                        factory.getByteSerializer(item).toBytes(item)
                )
        );

        return builder.toByteArray();
    }

    @Override
    public Class<Collection<?>> getType() {
        //noinspection unchecked,rawtypes
        return (Class) Collection.class;
    }
}
