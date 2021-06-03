package com.casper.sdk.domain;

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

    void append(final byte[] bytes) {
        try {
            if (bytes != null && bytes.length > 0) {
                out.write(bytes);
            }
        } catch (IOException e) {
            throw new ConversionException(e);
        }

    }

    byte[] toByteArray() {
        return out.toByteArray();
    }

    void append(final String hexString) {
        append(decodeHex(hexString));
    }
}
