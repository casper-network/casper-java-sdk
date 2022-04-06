package com.casper.sdk.service.serialization.types;

import com.casper.sdk.service.serialization.cltypes.TypesFactory;
import com.casper.sdk.types.Transfer;

/**
 * The byte serializer for {@link Transfer} deploy executables
 */
public class TransferByteSerializer extends AbstractDeployExecutableByteSerializer<Transfer> {

    public TransferByteSerializer(final ByteSerializerFactory factory, final TypesFactory typesFactory) {
        super(Transfer.class, factory, typesFactory);
    }

    @Override
    protected byte[] toSpecializedBytes(final Transfer deployExecutable) {
        return new byte[0];
    }
}
