package com.casper.sdk.e2e.utils;

import com.casper.sdk.e2e.exception.NotImplementedException;
import com.casper.sdk.e2e.exception.TestException;
import com.casper.sdk.model.clvalue.cltype.CLTypeData;
import com.casper.sdk.model.key.Key;
import com.casper.sdk.model.key.PublicKey;
import com.casper.sdk.model.uref.URef;
import com.casper.sdk.model.uref.URefAccessRight;
import com.syntifi.crypto.key.encdec.Hex;

import java.math.BigInteger;

/**
 * Utility methods for CLValue Types
 *
 * @author ian@meywood.com
 */
public class CLTypeUtils {

    /**
     * Converts a string the internal value of a CLValue
     *
     * @param typeName the name of the type
     * @param value    the string representation of the value
     * @return the CLValue's internal parsed value
     */
    public static Object convertToCLTypeValue(final String typeName, final String value) {
        try {
            switch (CLTypeData.getTypeByName(typeName)) {
                case STRING:
                    return value;

                case U8:
                    return Byte.parseByte(value);

                case U32:
                case I64:
                    return Long.valueOf(value);

                case U64:
                case U256:
                    return new BigInteger(value);

                case BOOL:
                    return Boolean.valueOf(value);

                case I32:
                    return Integer.valueOf(value);

                case BYTE_ARRAY:
                    return Hex.decode(value);

                case KEY:
                    return Key.fromTaggedHexString(value);

                case PUBLIC_KEY:
                    return PublicKey.fromTaggedHexString(value);

                case UREF:
                    return new URef(Hex.decode(value), URefAccessRight.READ_ADD_WRITE);

                default:
                    throw new NotImplementedException("Not implemented conversion for type " + typeName);
            }
        } catch (Exception e) {
            throw new TestException(e);
        }
    }
}
