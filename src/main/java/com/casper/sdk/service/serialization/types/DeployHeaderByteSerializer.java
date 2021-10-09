package com.casper.sdk.service.serialization.types;

import com.casper.sdk.types.CLType;
import com.casper.sdk.types.DeployHeader;
import com.casper.sdk.types.Digest;
import com.casper.sdk.types.CLPublicKey;
import com.casper.sdk.service.serialization.cltypes.TypesFactory;
import com.casper.sdk.service.serialization.cltypes.TypesSerializer;

import java.util.List;

import static com.casper.sdk.service.serialization.util.ByteUtils.concat;

/**
 * The byte serializer for {@link DeployHeader} type objects.
 */
class DeployHeaderByteSerializer implements ByteSerializer<DeployHeader> {

    private final ByteSerializerFactory factory;
    private final TypesSerializer u64Serializer;
    private final TypesSerializer stringSerializer;

    public DeployHeaderByteSerializer(final ByteSerializerFactory factory, final TypesFactory typesFactory) {
        this.factory = factory;
        u64Serializer = typesFactory.getInstance(CLType.U64);
        stringSerializer = typesFactory.getInstance(CLType.STRING);
    }

    @Override
    public byte[] toBytes(final DeployHeader source) {

        return concat(
                factory.getByteSerializerByType(CLPublicKey.class).toBytes(source.getAccount()),
                u64Serializer.serialize(source.getTimestamp()),
                u64Serializer.serialize(source.getTtl()),
                u64Serializer.serialize(source.getGasPrice()),
                // toBytesDeployHash
                factory.getByteSerializerByType(Digest.class).toBytes(source.getBodyHash()),
                // toBytesVecT
                factory.getByteSerializerByType(List.class).toBytes(source.getDependencies()),
                // toBytesString
                stringSerializer.serialize(source.getChainName())
        );
    }

    @Override
    public Class<DeployHeader> getType() {
        return DeployHeader.class;
    }
}
