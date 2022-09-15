package com.casper.sdk.service.serialization.types;

import com.casper.sdk.service.serialization.cltypes.TypesFactory;
import com.casper.sdk.service.serialization.util.ByteUtils;
import com.casper.sdk.types.CLByteArrayInfo;
import com.casper.sdk.types.CLValue;
import org.apache.commons.lang3.ArrayUtils;

import java.math.BigInteger;

/**
 * Converts a CLValue to a byte array
 */
class CLValueByteSerializer extends AbstractByteSerializer<CLValue> {

    public CLValueByteSerializer(final TypesFactory typesFactory) {
        super(typesFactory);
    }

    @Override
    public byte[] toBytes(final CLValue source) {

        final byte[] lengthBytes = getLengthBytes(source);
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

    /**
     * Obtains the length of the values bytes as a U32 4 byte array.
     *
     * @param source the value whose byte length is to be obtained
     * @return the length of the values bytes as a U32 4 byte array
     */
    private byte[] getLengthBytes(final CLValue source) {

        if (containsLengthBytes(source)) {
            // Don't supply a length as it is already present in the byte array
            return new byte[0];
        } else {
            return getU32Serializer().serialize(source.getBytes().length);
        }
    }

    /**
     * Indicates if the CLValues bytes are prefixed with th e length
     *
     * @param source the value to test if bytes are prefixed
     * @return true if the bytes are prefixed with a U32 4 byte length otherwise null
     */
    private boolean containsLengthBytes(final CLValue source) {
        if (source.getCLTypeInfo() instanceof CLByteArrayInfo) {
            // we already know the length so use existing length
            final int size = ((CLByteArrayInfo) source.getCLTypeInfo()).getSize();
            if (size >= 4) {
                final byte[] lb = new byte[4];
                // Obtain the length from the 1st four bytes U32 representation
                System.arraycopy(source.getBytes(), 0, lb, 0, 4);
                // Change from BE to LE (Java network order)
                ArrayUtils.reverse(lb);

                final int biLen = new BigInteger(lb).intValue();
                // Don't supply a length as it is already present in the byte array
                return biLen == size && source.getBytes().length - 4 == biLen;
            }
        }
        return false;
    }
}
