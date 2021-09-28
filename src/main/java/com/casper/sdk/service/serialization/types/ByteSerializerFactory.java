package com.casper.sdk.service.serialization.types;

import com.casper.sdk.service.serialization.cltypes.TypesFactory;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * The factory of all the {@link ByteSerializer} classes for type objects.
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
     * Obtains a {@link ByteSerializer} for the specified casper type object.
     *
     * @param source the type object to obtain a byte serializer for
     * @param <T>    the type of the type object
     * @return the byte serializer
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public <T> ByteSerializer<T> getByteSerializer(final T source) {
        return (ByteSerializer) getByteSerializerByType(source.getClass());
    }

    /**
     * Obtains a {@link ByteSerializer} for the specified casper type object class.
     *
     * @param type the class of type object to obtain a byte serializer for
     * @param <T>  the type of the object
     * @return the byte serializer
     */
    @SuppressWarnings({"unchecked", "SingleStatementInBlock"})
    public <T> ByteSerializer<T> getByteSerializerByType(final Class<T> type) {
        ByteSerializer<T> byteSerializer = (ByteSerializer<T>) serializerMap.get(type);
        if (byteSerializer == null) {
            final Class<?> superclass = type.getSuperclass();
            if (superclass != null && !superclass.equals(Object.class)) {
                byteSerializer = (ByteSerializer<T>) getByteSerializerByType(superclass);
            }
        }

        if (byteSerializer == null && Collection.class.isAssignableFrom(type)) {
            byteSerializer = (ByteSerializer<T>) getByteSerializerByType(Collection.class);
        }
        return byteSerializer;
    }

    protected void register(final ByteSerializer<?> byteSerializer) {
        serializerMap.put(byteSerializer.getType(), byteSerializer);
    }
}
