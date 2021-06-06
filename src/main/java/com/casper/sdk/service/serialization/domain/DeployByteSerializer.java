package com.casper.sdk.service.serialization.domain;

import com.casper.sdk.domain.Deploy;
import com.casper.sdk.domain.DeployNamedArg;
import com.casper.sdk.service.serialization.util.ByteArrayBuilder;
import org.apache.commons.lang3.NotImplementedException;

import java.util.List;

import static com.casper.sdk.service.serialization.util.ByteUtils.toU32;

/**
 * The byte serializater for a {@link Deploy} domain object
 */
class DeployByteSerializer implements ByteSerializer<Deploy> {

    private final ByteSerializerFactory factory;

    DeployByteSerializer(final ByteSerializerFactory factory) {
        this.factory = factory;
    }

    public byte[] toBytes(final Deploy deploy) {
        throw new NotImplementedException("TODO DeployByteSerializer");
        //return new byte[0];
    }

    @Override
    public Class<Deploy> getType() {
        return Deploy.class;
    }


}
