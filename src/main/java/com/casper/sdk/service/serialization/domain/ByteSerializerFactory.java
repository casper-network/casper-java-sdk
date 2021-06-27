package com.casper.sdk.service.serialization.domain;

import com.casper.sdk.service.serialization.cltypes.TypesFactory;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * The factory of all the {@link ByteSerializer} classes for domain objects.
 */
public class ByteSerializerFactory {

    /** Map of byte serializers */
    private final Map<Class<?>, ByteSerializer<?>> serializerMap = new HashMap<>();

    public ByteSerializerFactory() {
        final TypesFactory typesFactory = new TypesFactory();
        register(new CLValueByteSerializer(typesFactory));
        register(new CollectionByteSerializer(this, typesFactory));
        register(new DeployApprovalByteSerializer());
        register(new DeployByteSerializer(this));
        register(new DeployExecutableByteSerializer(this, typesFactory));
        register(new DeployHeaderByteSerializer(this, typesFactory));
        register(new DeployNamedArgByteSerializer(typesFactory));
        register(new DigestByteSerializer());
        register(new PublicKeyByteSerializer());
    }

    /**
     * Obtains a {@link ByteSerializer} for the specified casper domain object.
     *
     * @param source the domain object to obtain a byte serializer for
     * @param <T>    the type of the domain object
     * @return the byte serializer
     */
    public <T> ByteSerializer<T> getByteSerializer(final T source) {
        //noinspection unchecked,rawtypes
        return (ByteSerializer) getByteSerializerByType(source.getClass());
    }

    /**
     * Obtains a {@link ByteSerializer} for the specified casper domain object class.
     *
     * @param type the class of domain object to obtain a byte serializer for
     * @param <T>  the type of the domain object
     * @return the byte serializer
     */
    public <T> ByteSerializer<T> getByteSerializerByType(final Class<T> type) {
        //noinspection unchecked
        ByteSerializer<T> byteSerializer = (ByteSerializer<T>) serializerMap.get(type);
        if (byteSerializer == null) {
            final Class<?> superclass = type.getSuperclass();
            if (superclass != null && !superclass.equals(Object.class)) {
                //noinspection unchecked
                byteSerializer = (ByteSerializer<T>) getByteSerializerByType(superclass);
            }
        }

        if (byteSerializer == null && Collection.class.isAssignableFrom(type)) {
            //noinspection unchecked
            byteSerializer = (ByteSerializer<T>) getByteSerializerByType(Collection.class);
        }
        return byteSerializer;
    }

    protected void register(final ByteSerializer<?> byteSerializer) {
        serializerMap.put(byteSerializer.getType(), byteSerializer);
    }

}
