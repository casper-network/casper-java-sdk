package com.casper.sdk.service.serialization.types;

import com.casper.sdk.service.serialization.cltypes.TypesFactory;
import com.casper.sdk.service.serialization.cltypes.TypesSerializer;
import com.casper.sdk.service.serialization.util.ByteArrayBuilder;
import com.casper.sdk.types.*;

import java.util.List;

/**
 * The byte serializer for a {@link DeployExecutable} and it's extending classes.
 */
class DeployExecutableByteSerializer implements ByteSerializer<DeployExecutable> {

    private final ByteSerializerFactory factory;
    private final TypesSerializer byteArraySerializer;
    private final TypesSerializer optionSerializer;
    private final TypesSerializer stringSerializer;
    private final TypesSerializer u32Serializer;

    public DeployExecutableByteSerializer(final ByteSerializerFactory factory, final TypesFactory typesFactory) {
        this.factory = factory;
        byteArraySerializer = typesFactory.getInstance(CLType.BYTE_ARRAY);
        optionSerializer = typesFactory.getInstance(CLType.OPTION);
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
        final ByteArrayBuilder builder = new ByteArrayBuilder().append((byte) deployExecutable.getTag());

        if (deployExecutable instanceof StoredVersionedContractByName) {
            builder.append(storedContractVersionNameToBytes((StoredVersionedContractByName) deployExecutable));
        } else if (deployExecutable instanceof StoredContractByName) {
            builder.append(storedContractByNameToBytes((StoredContractByName) deployExecutable));
        } else if (deployExecutable instanceof StoredVersionedContractByHash) {
            builder.append(storedVersionedContractByHashToBytes((StoredVersionedContractByHash) deployExecutable));
        } else if (deployExecutable instanceof StoredContractByHash) {
            builder.append(storedContractByHashToBytes((StoredContractByHash) deployExecutable));
        } else if (deployExecutable instanceof ModuleBytes) {
            builder.append(byteArraySerializer.serialize(deployExecutable.getModuleBytes()));
        }

        // Append any args if present
        builder.append(namedAgsToBytes(deployExecutable.getArgs()));

        return builder.toByteArray();
    }

    private byte[] storedVersionedContractByHashToBytes(final StoredVersionedContractByHash storedVersionedContractByHash) {
        final ByteArrayBuilder builder = new ByteArrayBuilder();
        builder.append(storedVersionedContractByHash.getHash().getHash());
        storedVersionedContractByHash.getVersion().ifPresent(version -> builder.append(versionToBytes(version)));
        builder.append(stringSerializer.serialize(storedVersionedContractByHash.getEntryPoint()));
        return builder.toByteArray();
    }

    private byte[] storedContractByNameToBytes(final StoredContractByName storedContractByName) {
        final ByteArrayBuilder builder = new ByteArrayBuilder();
        builder.append(stringSerializer.serialize(storedContractByName.getName()))
                .append(stringSerializer.serialize(storedContractByName.getEntryPoint()));
        return builder.toByteArray();
    }

    private byte[] storedContractByHashToBytes(final StoredContractByHash storedContractByHash) {
        final ByteArrayBuilder builder = new ByteArrayBuilder();
        builder.append(storedContractByHash.getHash().getHash());
        builder.append(stringSerializer.serialize(storedContractByHash.getEntryPoint()));
        return builder.toByteArray();
    }

    private byte[] storedContractVersionNameToBytes(final StoredVersionedContractByName storedVersionedContractByName) {
        final ByteArrayBuilder builder = new ByteArrayBuilder();
        builder.append(stringSerializer.serialize(storedVersionedContractByName.getName()));

        storedVersionedContractByName.getVersion().ifPresent(version -> builder.append(versionToBytes(version)));

        builder.append(stringSerializer.serialize(storedVersionedContractByName.getEntryPoint()));
        return builder.toByteArray();
    }

    private byte[] versionToBytes(final Number version) {

        final CLOptionValue clOptionValue = new CLOptionValue(
                u32Serializer.serialize(version),
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
