package com.casper.sdk.service.serialization.domain;

import com.casper.sdk.domain.Deploy;
import com.casper.sdk.domain.DeployExecutable;
import com.casper.sdk.service.serialization.util.ByteArrayBuilder;
import com.casper.sdk.service.serialization.util.ByteUtils;

/**
 * The byte serializer for a {@link Deploy} domain object
 */
class DeployByteSerializer implements ByteSerializer<Deploy> {

    private final ByteSerializerFactory factory;

    DeployByteSerializer(final ByteSerializerFactory factory) {
        this.factory = factory;
    }

    public byte[] toBytes(final Deploy deploy) {

        final ByteArrayBuilder builder = new ByteArrayBuilder();

        builder.append(_toBytes(deploy.getHeader()))
                .append(_toBytes(deploy.getHash()))
                .append(serializeBody(deploy.getPayment(), deploy.getSession()))
                .append(_toBytes(deploy.getApprovals()));

        return builder.toByteArray();
    }

    byte[] serializeBody(final DeployExecutable payment, final DeployExecutable session) {
        return ByteUtils.concat(_toBytes(payment), _toBytes(session));
    }

    private byte[] _toBytes(final Object source) {
        return factory.getByteSerializer(source).toBytes(source);
    }

    @Override
    public Class<Deploy> getType() {
        return Deploy.class;
    }


}
