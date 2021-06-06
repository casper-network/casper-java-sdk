package com.casper.sdk.service.serialization.domain;

import com.casper.sdk.domain.DeployHeader;
import com.casper.sdk.domain.Digest;
import com.casper.sdk.service.serialization.util.ByteArrayBuilder;
import com.casper.sdk.service.serialization.util.ByteUtils;

import java.util.List;

import static com.casper.sdk.service.serialization.util.ByteUtils.toCLStringBytes;

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

        final ByteArrayBuilder builder = new ByteArrayBuilder();

        builder.append(source.getAccount().getBytes());
        builder.append(ByteUtils.toU64(source.getTtlLong()));
        builder.append(ByteUtils.toU64(source.getGasPrice()));
        builder.append(factory.getByteSerializerByType(Digest.class).toBytes(source.getBodyHash()));
        builder.append(factory.getByteSerializerByType(List.class).toBytes(source.getDependencies()));
        builder.append(toCLStringBytes(source.getChainName()));

        return builder.toByteArray();
    }

    @Override
    public Class<DeployHeader> getType() {
        return DeployHeader.class;
    }
}
