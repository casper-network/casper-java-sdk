package com.casper.sdk.service.serialization.domain;

import com.casper.sdk.domain.DeployHeader;
import com.casper.sdk.domain.Digest;
import com.casper.sdk.domain.PublicKey;

import java.util.List;

import static com.casper.sdk.service.serialization.util.ByteUtils.*;

/**
 * The byte serializer for {@link DeployHeader} domain objects.
 */
class DeployHeaderByteSerializer implements ByteSerializer<DeployHeader> {

    private final ByteSerializerFactory factory;

    public DeployHeaderByteSerializer(final ByteSerializerFactory factory) {
        this.factory = factory;
    }

    @Override
    public byte[] toBytes(final DeployHeader source) {

        return concat(
                factory.getByteSerializerByType(PublicKey.class).toBytes(source.getAccount()),
                toU64(source.getTimestamp()),
                toU64(source.getTtl()),
                toU64(source.getGasPrice()),
                factory.getByteSerializerByType(Digest.class).toBytes(source.getBodyHash()),
                factory.getByteSerializerByType(List.class).toBytes(source.getDependencies()),
                toCLStringBytes(source.getChainName())
        );
    }

    @Override
    public Class<DeployHeader> getType() {
        return DeployHeader.class;
    }
}
