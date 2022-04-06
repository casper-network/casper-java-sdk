package com.casper.sdk.service.serialization.types;

import com.casper.sdk.service.serialization.cltypes.TypesFactory;
import com.casper.sdk.service.serialization.util.ByteArrayBuilder;
import com.casper.sdk.types.StoredContractByName;

/**
 * The byte serializer for {@link StoredContractByName} deploy executables
 */
public class StoredContractByNameByteSerializer extends AbstractDeployExecutableByteSerializer<StoredContractByName> {

    public StoredContractByNameByteSerializer(final ByteSerializerFactory factory, final TypesFactory typesFactory) {
        super(StoredContractByName.class, factory, typesFactory);
    }

    @Override
    protected byte[] toSpecializedBytes(final StoredContractByName storedContractByName) {
        return new ByteArrayBuilder()
                .append(stringSerializer.serialize(storedContractByName.getName()))
                .append(stringSerializer.serialize(storedContractByName.getEntryPoint()))
                .toByteArray();
    }
}
