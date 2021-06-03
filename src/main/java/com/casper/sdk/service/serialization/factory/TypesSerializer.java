package com.casper.sdk.service.serialization.factory;


import java.math.BigInteger;

public interface TypesSerializer {

    byte[] serialize(final Object toSerialize);

    /**
     * Converts a source object to ta {@link BigInteger}.
     *
     * @param source the source to convert
     * @return a big integer
     */
    default BigInteger toBigInteger(final Object source) {

        final BigInteger bigInt;

        if (source instanceof BigInteger) {
            bigInt = (BigInteger) source;
        } else if (source instanceof Number) {
            bigInt = new BigInteger(source.toString());
        } else if (source instanceof String && ((String) source).length() > 0) {
            bigInt = new BigInteger(String.valueOf(source));
        } else {
            bigInt = BigInteger.valueOf(0L);
        }
        return bigInt;
    }
}
