package com.casper.sdk.service.serialization.types;

import com.casper.sdk.types.*;
import com.casper.sdk.service.serialization.cltypes.TypesFactory;
import com.casper.sdk.service.serialization.cltypes.TypesSerializer;

import java.nio.charset.StandardCharsets;
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
        byte[] encodedPublicKey = factory.getByteSerializerByType(CLPublicKey.class).toBytes(source.getAccount());
        //Fix for serialization always giving an ED key type on the header.
        if(source.getAccount().getAlgorithm().equals(Algorithm.SECP256K1)){
            encodedPublicKey[0] = 0x02;
        }
        return concat(
                encodedPublicKey,
                u64Serializer.serialize(source.getTimestamp()),
                u64Serializer.serialize(source.getTtl()),
                u64Serializer.serialize(source.getGasPrice()),
                // toBytesDeployHash
                //source.getBodyHash().getHash(),
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
