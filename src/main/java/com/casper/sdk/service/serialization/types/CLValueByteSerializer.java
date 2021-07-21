package com.casper.sdk.service.serialization.types;

import com.casper.sdk.types.CLValue;
import com.casper.sdk.service.serialization.cltypes.TypesFactory;
import com.casper.sdk.service.serialization.util.ByteUtils;

/**
 * Converts a CLValue to a byte array
 */
class CLValueByteSerializer extends AbstractByteSerializer<CLValue> {

    public CLValueByteSerializer(final TypesFactory typesFactory) {
        super(typesFactory);
    }

    @Override
    public byte[] toBytes(final CLValue source) {
        return ByteUtils.concat(
                getU32Serializer().serialize(source.getBytes().length),
                source.getBytes(),
                toBytesForCLTypeInfo(source.getCLTypeInfo())
        );
    }

    @Override
    public Class<CLValue> getType() {
        return CLValue.class;
    }
}
