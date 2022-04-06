package com.casper.sdk.service.serialization.types;

import com.casper.sdk.service.serialization.cltypes.TypesFactory;
import com.casper.sdk.service.serialization.util.ByteArrayBuilder;
import com.casper.sdk.types.StoredContractByHash;

/**
 * The byte serializer for {@link StoredContractByHash} deploy executables
 */
public class StoredContractByHashByteSerializer extends AbstractDeployExecutableByteSerializer<StoredContractByHash> {

    public StoredContractByHashByteSerializer(final ByteSerializerFactory factory, final TypesFactory typesFactory) {
        super(StoredContractByHash.class, factory, typesFactory);
    }

    @Override
    protected byte[] toSpecializedBytes(final StoredContractByHash storedContractByHash) {
        return new ByteArrayBuilder()
                .append(storedContractByHash.getHash().getHash())
                .append(stringSerializer.serialize(storedContractByHash.getEntryPoint()))
                .toByteArray();
    }
}
