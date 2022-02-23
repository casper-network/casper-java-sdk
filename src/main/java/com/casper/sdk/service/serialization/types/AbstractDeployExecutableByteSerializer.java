package com.casper.sdk.service.serialization.types;

import com.casper.sdk.service.serialization.cltypes.TypesFactory;
import com.casper.sdk.service.serialization.cltypes.TypesSerializer;
import com.casper.sdk.service.serialization.util.ByteArrayBuilder;
import com.casper.sdk.types.*;

import java.util.List;

/**
 * The Abstract byte serializer for all classes that extend {@link DeployExecutable}.
 */
abstract class AbstractDeployExecutableByteSerializer<T extends DeployExecutable> implements ByteSerializer<T> {

    protected final TypesSerializer byteArraySerializer;
    protected final TypesSerializer stringSerializer;
    private final Class<T> type;
    private final ByteSerializerFactory factory;
    private final TypesSerializer optionSerializer;
    private final TypesSerializer u32Serializer;

    public AbstractDeployExecutableByteSerializer(final Class<T> type, final ByteSerializerFactory factory, final TypesFactory typesFactory) {
        this.type = type;
        this.factory = factory;
        byteArraySerializer = typesFactory.getInstance(CLType.BYTE_ARRAY);
        optionSerializer = typesFactory.getInstance(CLType.OPTION);
        stringSerializer = typesFactory.getInstance(CLType.STRING);
        u32Serializer = typesFactory.getInstance(CLType.U32);
    }

    @Override
    public Class<T> getType() {
        return type;
    }

    @Override
    public byte[] toBytes(final T deployExecutable) {

        // Append the type of the 'Deploy Executable' in a single byte
        return new ByteArrayBuilder()
                .append((byte) deployExecutable.getTag())
                .append(toSpecializedBytes(deployExecutable))
                .append(namedAgsToBytes(deployExecutable.getArgs()))
                .toByteArray();
    }

    protected abstract byte[] toSpecializedBytes(final T deployExecutable);

    protected byte[] versionToBytes(final Number version) {

        final CLOptionValue clOptionValue = new CLOptionValue(
                version != null ? u32Serializer.serialize(version) : null,
                new CLOptionTypeInfo(new CLTypeInfo(CLType.U32)),
                version
        );
        return optionSerializer.serialize(clOptionValue);
    }

    private byte[] namedAgsToBytes(final List<DeployNamedArg> args) {

        final ByteArrayBuilder builder = new ByteArrayBuilder();

        // append the number of arguments as LE U32 array
        builder.append(u32Serializer.serialize(args.size()));

        // Append each argument
        args.forEach(deployNamedArg ->
                builder.append(factory.getByteSerializer(deployNamedArg).toBytes(deployNamedArg))
        );
        return builder.toByteArray();
    }
}
