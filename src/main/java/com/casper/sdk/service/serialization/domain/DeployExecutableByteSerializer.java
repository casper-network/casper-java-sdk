package com.casper.sdk.service.serialization.domain;

import com.casper.sdk.domain.*;
import com.casper.sdk.service.serialization.util.ByteArrayBuilder;

import java.util.List;

import static com.casper.sdk.service.serialization.util.ByteUtils.toCLStringBytes;
import static com.casper.sdk.service.serialization.util.ByteUtils.toU32;

/**
 * The byte serializer for a {@link DeployExecutable} and it's extending classes.
 */
class DeployExecutableByteSerializer implements ByteSerializer<DeployExecutable> {

    private final ByteSerializerFactory factory;

    public DeployExecutableByteSerializer(final ByteSerializerFactory factory) {
        this.factory = factory;
    }

    @Override
    public byte[] toBytes(final DeployExecutable deployExecutable) {

        final ByteArrayBuilder builder = new ByteArrayBuilder();

        // Append the type of the Deploy Executable in a single byte
        builder.append((byte) deployExecutable.getTag());

        if (deployExecutable instanceof ModuleBytes) {
            builder.append(deployExecutable.getModuleBytes());
        } else if (deployExecutable instanceof StoredContractByName) {
            builder.append(toCLStringBytes(((StoredContractByName) deployExecutable).getName()))
                    .append(toCLStringBytes(((StoredContractByName) deployExecutable).getEntryPoint()))
                    .append(toBytes(deployExecutable.getArgs()));
        } else if (deployExecutable instanceof Transfer) {
            builder.append(toBytes(deployExecutable.getArgs()));
        }

        return builder.toByteArray();
    }


    @Override
    public Class<DeployExecutable> getType() {
        return DeployExecutable.class;
    }

    private byte[] toBytes(final List<DeployNamedArg> args) {

        final ByteArrayBuilder builder = new ByteArrayBuilder();

        // append the number of arguments as LE U32 array
        builder.append(toU32(args.size()));

        // Append each argument
        args.forEach(deployNamedArg ->
                factory.getByteSerializer(deployNamedArg).toBytes(deployNamedArg)
        );
        return builder.toByteArray();
    }

}
