package com.casper.sdk.service.serialization.types;

import com.casper.sdk.service.serialization.cltypes.TypesFactory;
import com.casper.sdk.service.serialization.util.ByteArrayBuilder;
import com.casper.sdk.types.StoredVersionedContractByName;

/**
 * The byte serializer for {@link StoredVersionedContractByName} deploy executables
 */
public class StoredVersionedContractByNameByteSerializer extends AbstractDeployExecutableByteSerializer<StoredVersionedContractByName> {

    public StoredVersionedContractByNameByteSerializer(final ByteSerializerFactory factory,
                                                       final TypesFactory typesFactory) {
        super(StoredVersionedContractByName.class, factory, typesFactory);
    }

    @Override
    protected byte[] toSpecializedBytes(final StoredVersionedContractByName storedVersionedContractByName) {
        return new ByteArrayBuilder()
                .append(stringSerializer.serialize(storedVersionedContractByName.getName()))
                .append(versionToBytes(storedVersionedContractByName.getVersion().orElse(null)))
                .append(stringSerializer.serialize(storedVersionedContractByName.getEntryPoint()))
                .toByteArray();
    }
}
