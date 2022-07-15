package com.casper.sdk.service.serialization.types;

import com.casper.sdk.service.serialization.cltypes.TypesFactory;
import com.casper.sdk.service.serialization.util.ByteUtils;
import com.casper.sdk.types.CLValue;

/**
 * Converts a CLValue to a byte array
 */
class CLValueByteSerializer extends AbstractByteSerializer<CLValue> {

    public CLValueByteSerializer(final TypesFactory typesFactory) {
        super(typesFactory);
    }

    @Override
    public byte[] toBytes(final CLValue source) {

        final byte[] lengthBytes = getU32Serializer().serialize(source.getBytes().length);
        final byte[] sourceBytes = source.getBytes();
        final byte[] typeInfoBytes = toBytesForCLTypeInfo(source.getCLTypeInfo());

        return ByteUtils.concat(
                lengthBytes,
                sourceBytes,
                typeInfoBytes
        );
    }

    @Override
    public Class<CLValue> getType() {
        return CLValue.class;
    }
}
