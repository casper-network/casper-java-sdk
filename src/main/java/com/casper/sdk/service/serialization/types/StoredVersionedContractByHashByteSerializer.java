package com.casper.sdk.service.serialization.types;

import com.casper.sdk.service.serialization.cltypes.TypesFactory;
import com.casper.sdk.service.serialization.util.ByteArrayBuilder;
import com.casper.sdk.types.StoredVersionedContractByHash;

/**
 * The byte serializer for {@link StoredVersionedContractByHash} deploy executables
 */
public class StoredVersionedContractByHashByteSerializer extends AbstractDeployExecutableByteSerializer<StoredVersionedContractByHash> {

    public StoredVersionedContractByHashByteSerializer(final ByteSerializerFactory factory,
                                                       final TypesFactory typesFactory) {
        super(StoredVersionedContractByHash.class, factory, typesFactory);
    }

    @Override
    protected byte[] toSpecializedBytes(final StoredVersionedContractByHash storedVersionedContractByHash) {
        return new ByteArrayBuilder()
                .append(byteArraySerializer.serialize(storedVersionedContractByHash.getHash()))
                .append(versionToBytes(storedVersionedContractByHash.getVersion().orElse(null)))
                .append(stringSerializer.serialize(storedVersionedContractByHash.getEntryPoint()))
                .toByteArray();
    }
}
