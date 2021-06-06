package com.casper.sdk.service.serialization.cltypes;

import com.casper.sdk.service.serialization.util.ByteUtils;
import org.apache.commons.lang3.ArrayUtils;

import java.math.BigInteger;
import java.nio.ByteBuffer;

import static com.casper.sdk.service.serialization.util.NumberUtils.toBigInteger;

class U64 implements TypesSerializer {

    @Override
    public byte[] serialize(final Object toSerialize) {

        final BigInteger bigInt = toBigInteger(toSerialize);

        if (bigInt.longValueExact() == 0L) {
            //00 no value
            return new byte[]{0};
        }

        final long longValue = bigInt.longValue();

        final ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.putLong(longValue);
        final byte[] bytes = buffer.array();

        ArrayUtils.reverse(bytes);

        //append optional 01/00 to returned vale
        //01 has value
        //00 no value
        return ByteUtils.concat(new byte[]{1}, bytes);
    }
}
