package com.casper.sdk.service.serialization.types;

import com.casper.sdk.types.*;
import com.casper.sdk.service.serialization.cltypes.TypesFactory;
import com.casper.sdk.service.serialization.cltypes.TypesSerializer;
import com.casper.sdk.service.serialization.util.ByteArrayBuilder;

import java.util.List;

/**
 * The byte serializer for a {@link DeployExecutable} and it's extending classes.
 */
class DeployExecutableByteSerializer implements ByteSerializer<DeployExecutable> {

    private final ByteSerializerFactory factory;
    private final TypesSerializer byteArraySerializer;
    private final TypesSerializer stringSerializer;
    private final TypesSerializer u32Serializer;

    public DeployExecutableByteSerializer(final ByteSerializerFactory factory, final TypesFactory typesFactory) {
        this.factory = factory;
        byteArraySerializer = typesFactory.getInstance(CLType.BYTE_ARRAY);
        stringSerializer = typesFactory.getInstance(CLType.STRING);
        u32Serializer = typesFactory.getInstance(CLType.U32);
    }

    @Override
    public Class<DeployExecutable> getType() {
        return DeployExecutable.class;
    }

    @Override
    public byte[] toBytes(final DeployExecutable deployExecutable) {

        // Append the type of the 'Deploy Executable' in a single byte
        final ByteArrayBuilder builder = new ByteArrayBuilder()
                .append((byte) deployExecutable.getTag());

        if (deployExecutable instanceof StoredContractByName) {
            builder.append(stringSerializer.serialize(((StoredContractByName) deployExecutable).getName()))
                    .append(stringSerializer.serialize(((StoredContractByName) deployExecutable).getEntryPoint()));
        } else if (deployExecutable instanceof StoredContractByHash) {
            builder.append(((StoredContractByHash) deployExecutable).getHash().getHash());
            builder.append(stringSerializer.serialize(((StoredContractByHash) deployExecutable).getEntryPoint()));
        } else if (deployExecutable instanceof ModuleBytes) {
            builder.append(byteArraySerializer.serialize(deployExecutable.getModuleBytes()));
        }

        // Append any args if present
        builder.append(toBytes(deployExecutable.getArgs()));

        return builder.toByteArray();
    }

    private byte[] toBytes(final List<DeployNamedArg> args) {

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
