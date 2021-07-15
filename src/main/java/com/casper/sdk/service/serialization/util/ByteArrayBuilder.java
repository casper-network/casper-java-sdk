package com.casper.sdk.service.serialization.util;

import com.casper.sdk.exceptions.ConversionException;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Helper class for building byte arrays
 */
public class ByteArrayBuilder {

    private final ByteArrayOutputStream out = new ByteArrayOutputStream();

    private static byte[] decodeHex(final String serialize) {
        try {
            return Hex.decodeHex(serialize.toCharArray());
        } catch (DecoderException e) {
            throw new ConversionException("Unable to decode " + serialize, e);
        }
    }

    public ByteArrayBuilder append(final byte[] bytes) {
        try {
            if (bytes != null && bytes.length > 0) {
                out.write(bytes);
            }
            return this;
        } catch (IOException e) {
            throw new ConversionException(e);
        }
    }

    public ByteArrayBuilder append(final byte aByte) {
        out.write(aByte);
        return this;
    }

    public ByteArrayBuilder append(final String hexString) {
        return append(decodeHex(hexString));
    }

    public byte[] toByteArray() {
        return out.toByteArray();
    }
}
