package com.casper.sdk.service.serialization.types;

import com.casper.sdk.types.Deploy;
import com.casper.sdk.types.DeployExecutable;
import com.casper.sdk.service.serialization.util.ByteArrayBuilder;
import com.casper.sdk.service.serialization.util.ByteUtils;

/**
 * The byte serializer for a {@link Deploy} type object
 */
class DeployByteSerializer implements ByteSerializer<Deploy> {

    /** The factory to delegate conversion of fields to */
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

    @Override
    public Class<Deploy> getType() {
        return Deploy.class;
    }

    byte[] serializeBody(final DeployExecutable payment, final DeployExecutable session) {
        return ByteUtils.concat(_toBytes(payment), _toBytes(session));
    }

    private byte[] _toBytes(final Object source) {
        return factory.getByteSerializer(source).toBytes(source);
    }
}
