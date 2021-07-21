package com.casper.sdk.service.serialization.types;

import com.casper.sdk.types.CLType;
import com.casper.sdk.service.serialization.cltypes.TypesFactory;
import com.casper.sdk.service.serialization.cltypes.TypesSerializer;
import com.casper.sdk.service.serialization.util.ByteArrayBuilder;

import java.util.Collection;

/**
 * The byte serializer for Lists of casper type objects.
 */
class CollectionByteSerializer implements ByteSerializer<Collection<?>> {

    private final ByteSerializerFactory factory;
    private final TypesSerializer u32Serializer;

    public CollectionByteSerializer(final ByteSerializerFactory factory, final TypesFactory typesFactory) {
        this.factory = factory;
        u32Serializer = typesFactory.getInstance(CLType.U32);
    }

    @Override
    public byte[] toBytes(Collection<?> source) {

        final ByteArrayBuilder builder = new ByteArrayBuilder();

        // Write the size of the list as the 1st 4 bytes
        builder.append(u32Serializer.serialize(source.size()));

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
