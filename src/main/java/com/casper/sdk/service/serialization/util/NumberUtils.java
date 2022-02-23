package com.casper.sdk.service.serialization.util;

import java.math.BigDecimal;
import java.math.BigInteger;

public class NumberUtils {

    /**
     * Converts a source object to ta {@link BigInteger}.
     *
     * @param source the source to convert
     * @return a big integer
     */
    public static BigInteger toBigInteger(final Object source) {

        final BigInteger bigInt;

        if (source instanceof BigInteger) {
            bigInt = (BigInteger) source;
        } else if (source instanceof Number) {
            if (source instanceof Double) {
                bigInt = BigDecimal.valueOf((Double) source).toBigInteger();
            } else {
                bigInt = new BigInteger(source.toString());
            }
        } else if (source instanceof String && ((String) source).length() > 0) {
            bigInt = new BigInteger(String.valueOf(source));
        } else {
            bigInt = BigInteger.valueOf(0L);
        }
        return bigInt;
    }
}
