package com.casper.sdk.service.serialization.domain;

import com.casper.sdk.domain.*;
import com.casper.sdk.service.serialization.cltypes.TypesFactory;
import com.casper.sdk.service.serialization.cltypes.TypesSerializer;
import com.casper.sdk.service.serialization.util.ByteArrayBuilder;

import java.util.List;

/**
 * The byte serializer for a {@link DeployExecutable} and it's extending classes.
 */
class DeployExecutableByteSerializer implements ByteSerializer<DeployExecutable> {

    private final ByteSerializerFactory factory;
    private final TypesSerializer u32Serializer;
    private final TypesSerializer stringSerializer;

    public DeployExecutableByteSerializer(final ByteSerializerFactory factory, final TypesFactory typesFactory) {
        this.factory = factory;
        u32Serializer = typesFactory.getInstance(CLType.U32);
        stringSerializer = typesFactory.getInstance(CLType.STRING);
    }

    @Override
    public byte[] toBytes(final DeployExecutable deployExecutable) {

        // Append the type of the Deploy Executable in a single byte
        final ByteArrayBuilder builder = new ByteArrayBuilder()
                .append((byte) deployExecutable.getTag());

        if (deployExecutable instanceof StoredContractByName) {
            builder.append(stringSerializer.serialize(((StoredContractByName) deployExecutable).getName()))
                    .append(stringSerializer.serialize(((StoredContractByName) deployExecutable).getEntryPoint()));
        } else if (deployExecutable instanceof ModuleBytes) {
            builder.append(u32Serializer.serialize(deployExecutable.getModuleBytes()));
        }

        // Append any args if present
        builder.append(toBytes(deployExecutable.getArgs()));

        return builder.toByteArray();
    }


    @Override
    public Class<DeployExecutable> getType() {
        return DeployExecutable.class;
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
