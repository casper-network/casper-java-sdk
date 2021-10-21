package com.syntifi.casper.sdk.model.clvalue.encdec;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;

import com.syntifi.casper.sdk.exception.BufferEndCLValueDecodeException;
import com.syntifi.casper.sdk.exception.CLValueDecodeException;
import com.syntifi.casper.sdk.exception.InvalidByteStringException;
import com.syntifi.casper.sdk.exception.NoSuchKeyTagException;
import com.syntifi.casper.sdk.model.clvalue.AbstractCLValue;
import com.syntifi.casper.sdk.model.clvalue.CLValueAny;
import com.syntifi.casper.sdk.model.clvalue.CLValueBool;
import com.syntifi.casper.sdk.model.clvalue.CLValueByteArray;
import com.syntifi.casper.sdk.model.clvalue.CLValueI32;
import com.syntifi.casper.sdk.model.clvalue.CLValueI64;
import com.syntifi.casper.sdk.model.clvalue.CLValueKey;
import com.syntifi.casper.sdk.model.clvalue.CLValuePublicKey;
import com.syntifi.casper.sdk.model.clvalue.CLValueString;
import com.syntifi.casper.sdk.model.clvalue.CLValueU128;
import com.syntifi.casper.sdk.model.clvalue.CLValueU256;
import com.syntifi.casper.sdk.model.clvalue.CLValueU32;
import com.syntifi.casper.sdk.model.clvalue.CLValueU512;
import com.syntifi.casper.sdk.model.clvalue.CLValueU64;
import com.syntifi.casper.sdk.model.clvalue.CLValueU8;
import com.syntifi.casper.sdk.model.clvalue.cltype.CLTypeData;
import com.syntifi.casper.sdk.model.key.Key;
import com.syntifi.casper.sdk.model.key.PublicKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Casper CLValue Decoding methods
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @see AbstractCLValue
 * @since 0.0.1
 */
public class CLValueDecoder extends ByteArrayInputStream {

    private static final Logger LOGGER = LoggerFactory.getLogger(CLValueDecoder.class);

    private static final String LOG_BUFFER_INIT_MESSAGE_STRING = "Initializing with hexString: {}";
    private static final String LOG_BUFFER_VALUE_MESSAGE_STRING = "Buffer value: {}";
    private static final String LOG_DECODED_VALUE_MESSAGE_STRING = "Decoded value for {}: {}";
    private static final String DECODE_EXCEPTION_BUFFER_END_EMPTY_MESSAGE_STRING = "Buffer empty, could not read data";
    private static final String DECODE_EXCEPTION_BUFFER_END_MESSAGE_STRING = "Buffer ended, could not read more data";
    private static final String DECODE_EXCEPTION_WRONG_LENGHT_MESSAGE_STRING = "Could not read %s (Expected length: %d, Actual length: %d)";
    private static final String DECODE_EXCEPTION_OUT_OF_BOUNDS_MESSAGE_STRING = "Value %s out of bounds for expected type %s";

    /**
     * Initializes buffer with decoded bytes from hex-encoded {@link String}
     * 
     * @param hexString hex-encoded {@link String} of a CLValue
     * @throws InvalidByteStringException
     */
    public CLValueDecoder(String hexString) throws InvalidByteStringException {
        super(StringByteHelper.hexStringToByteArray(hexString));

        LOGGER.debug(LOG_BUFFER_INIT_MESSAGE_STRING, hexString);
    }

    /**
     * Boolean values serialize as a single byte; true maps to 1, while false maps
     * to 0.
     * 
     * @return
     * @throws IOException
     * @throws CLValueDecodeException
     */
    public void readBool(CLValueBool clValue) throws IOException, CLValueDecodeException {
        int length = 1;
        byte[] buf = new byte[length];

        int readBytes = 0;
        if ((readBytes = this.read(buf)) != length) {
            throwReadBytesError(Boolean.class.getSimpleName(), length, readBytes);
        }

        LOGGER.debug(LOG_BUFFER_VALUE_MESSAGE_STRING, buf);

        clValue.setBytes(StringByteHelper.convertBytesToHex(buf));
        if (buf[0] == 1) {
            clValue.setValue(true);
        } else if (buf[0] == 0) {
            clValue.setValue(false);
        } else {
            throw new CLValueDecodeException(
                    String.format(DECODE_EXCEPTION_OUT_OF_BOUNDS_MESSAGE_STRING, buf[0], CLTypeData.BOOL));
        }
    }

    /**
     * Numeric values consisting of 64 bits or less serialize in the two’s
     * complement representation with little-endian byte order, and the appropriate
     * number of bytes for the bit-width.
     * 
     * E.g. 7u8 serializes as 0x07
     * 
     * @param clValue target {@link CLValueU8}
     * @throws IOException
     * @throws CLValueDecodeException
     */
    public void readU8(CLValueU8 clValue) throws IOException, CLValueDecodeException {
        byte u8 = readByte();

        LOGGER.debug(LOG_DECODED_VALUE_MESSAGE_STRING, Byte.class.getSimpleName(), u8);

        clValue.setBytes(StringByteHelper.convertBytesToHex(new byte[] { u8 }));
        clValue.setValue(u8);
    }

    public void readByteArray(CLValueByteArray clValue, int length) throws CLValueDecodeException, IOException {
        byte[] bytes = readBytes(length);

        LOGGER.debug(LOG_DECODED_VALUE_MESSAGE_STRING, Byte.class.getSimpleName(), bytes);

        clValue.setBytes(StringByteHelper.convertBytesToHex(bytes));
        clValue.setValue(bytes);
    }

    /**
     * Numeric values consisting of 64 bits or less serialize in the two’s
     * complement representation with little-endian byte order, and the appropriate
     * number of bytes for the bit-width.
     * 
     * E.g. 7u32 serializes as 0x07000000 E.g. 1024u32 serializes as 0x00040000
     * 
     * @param clValue target {@link CLValueI32}
     * @throws IOException
     * @throws CLValueDecodeException
     */
    public void readI32(CLValueI32 clValue) throws IOException, CLValueDecodeException {
        int length = 4;
        byte[] buf = new byte[length];

        int readBytes = 0;
        if ((readBytes = this.read(buf)) != length) {
            throwReadBytesError(Integer.class.getSimpleName(), length, readBytes);
        }

        clValue.setBytes(StringByteHelper.convertBytesToHex(buf));

        LOGGER.debug(LOG_BUFFER_VALUE_MESSAGE_STRING, buf);

        int integerNumber = 0;
        for (int i = 0; i < length; i++) {
            integerNumber += (buf[i] & 0xFF) << (8 * i);
        }

        LOGGER.debug(LOG_DECODED_VALUE_MESSAGE_STRING, Integer.class.getSimpleName(), integerNumber);
        clValue.setValue(integerNumber);
    }

    /**
     * Reads a {@link Long} value from buffer, representing an Unsigned
     * {@link Integer} (U32)
     * 
     * @param clValue target {@link CLValueU32}
     * @throws IOException
     * @throws CLValueDecodeException
     */
    public void readU32(CLValueU32 clValue) throws IOException, CLValueDecodeException {
        int length = 4;
        byte[] buf = new byte[length];

        int readBytes = 0;
        if ((readBytes = this.read(buf)) != length) {
            throwReadBytesError(Long.class.getSimpleName(), length, readBytes);
        }

        clValue.setBytes(StringByteHelper.convertBytesToHex(buf));

        LOGGER.debug(LOG_BUFFER_VALUE_MESSAGE_STRING, buf);

        int integerNumber = 0;
        for (int i = 0; i < length; i++) {
            integerNumber += (buf[i] & 0xFF) << (8 * i);
        }

        long unsignedInteger = Integer.toUnsignedLong(integerNumber);

        LOGGER.debug(LOG_DECODED_VALUE_MESSAGE_STRING, Long.class.getSimpleName(), unsignedInteger);
        clValue.setValue(unsignedInteger);
    }

    /**
     * Numeric values consisting of 64 bits or less serialize in the two’s
     * complement representation with little-endian byte order, and the appropriate
     * number of bytes for the bit-width.
     * 
     * E.g. 7u8 serializes as 0x07 E.g. 7u32 serializes as 0x07000000 E.g. 1024u32
     * serializes as 0x00040000
     * 
     * @param clValue target {@link CLValueI64}
     * @throws IOException
     * @throws CLValueDecodeException
     */
    public void readI64(CLValueI64 clValue) throws IOException, CLValueDecodeException {
        int length = 8;
        byte[] buf = new byte[length];

        int readBytes = 0;
        if ((readBytes = this.read(buf)) != length) {
            throwReadBytesError(Long.class.getSimpleName(), length, readBytes);
        }

        clValue.setBytes(StringByteHelper.convertBytesToHex(buf));

        LOGGER.debug(LOG_BUFFER_VALUE_MESSAGE_STRING, buf);

        StringByteHelper.reverse(buf);

        String longStringHex = StringByteHelper.convertBytesToHex(buf);

        Long longNumber = Long.parseLong(longStringHex, 16);

        LOGGER.debug(LOG_DECODED_VALUE_MESSAGE_STRING, Long.class.getSimpleName(), longNumber);
        clValue.setValue(longNumber);
    }

    /**
     * Reads a {@link BigInteger} value from buffer, representing an Unsigned
     * {@link Long} (U32)
     * 
     * @param clValue target {@link CLValueU64}
     * @throws IOException
     * @throws CLValueDecodeException
     */
    public void readU64(CLValueU64 clValue) throws IOException, CLValueDecodeException {
        int length = 8;
        byte[] buf = new byte[length];

        int readBytes = 0;
        if ((readBytes = this.read(buf)) != length) {
            throwReadBytesError(BigInteger.class.getSimpleName(), length, readBytes);
        }

        clValue.setBytes(StringByteHelper.convertBytesToHex(buf));

        StringByteHelper.reverse(buf);

        LOGGER.debug(LOG_BUFFER_VALUE_MESSAGE_STRING, buf);

        BigInteger unsignedLong;
        // Since this is a positive (unsigned) number, we should prefix with a zero
        // byte to parse correctly
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            outputStream.write(0);
            outputStream.write(buf);

            unsignedLong = new BigInteger(outputStream.toByteArray());
        }

        LOGGER.debug(LOG_DECODED_VALUE_MESSAGE_STRING, BigInteger.class.getSimpleName(), unsignedLong);

        clValue.setValue(unsignedLong);
    }

    /**
     * Reads U128 from buffer
     * 
     * @param clValue target {@link CLValueU128}
     * @throws IOException
     * @throws CLValueDecodeException
     */
    public void readU128(CLValueU128 clValue) throws IOException, CLValueDecodeException {
        this.readBigInteger(clValue);
    }

    /**
     * Reads U256 from buffer
     * 
     * @param clValue target {@link CLValueU256}
     * @throws IOException
     * @throws CLValueDecodeException
     */
    public void readU256(CLValueU256 clValue) throws IOException, CLValueDecodeException {
        this.readBigInteger(clValue);
    }

    /**
     * Reads U512 from buffer
     * 
     * @param clValue target {@link CLValueU512}
     * @throws IOException
     * @throws CLValueDecodeException
     */
    public void readU512(CLValueU512 clValue) throws IOException, CLValueDecodeException {
        this.readBigInteger(clValue);
    }

    /**
     * Wider numeric values (i.e. U128, U256, U512) serialize as one byte given the
     * length of the next number (in bytes), followed by the two’s complement
     * representation with little-endian byte order. The number of bytes should be
     * chosen as small as possible to represent the given number. This is done to
     * reduce the serialization size when small numbers are represented within a
     * wide data type.
     * 
     * E.g. U512::from(7) serializes as 0x0107 E.g. U512::from(1024) serializes as
     * 0x020004 E.g. U512::from("123456789101112131415") serializes as
     * 0x0957ff1ada959f4eb106
     * 
     * @param clValue target CLValue
     * @throws IOException
     * @throws CLValueDecodeException
     */
    protected void readBigInteger(AbstractCLValue<BigInteger, ?> clValue) throws IOException, CLValueDecodeException {
        byte[] buf = new byte[1];

        int readBytes = 0;
        if ((readBytes = this.read(buf)) != 1) {
            throw new CLValueDecodeException(String.format(DECODE_EXCEPTION_WRONG_LENGHT_MESSAGE_STRING,
                    Byte.class.getSimpleName(), 1, readBytes));
        }

        byte lengthOfNextNumber = buf[0];

        LOGGER.debug("Length of next number: {}", lengthOfNextNumber);

        buf = new byte[lengthOfNextNumber];

        if ((readBytes = this.read(buf)) != lengthOfNextNumber) {
            throwReadBytesError(BigInteger.class.getSimpleName(), lengthOfNextNumber, readBytes);
        }

        clValue.setBytes(StringByteHelper.convertBytesToHex(new byte[] { lengthOfNextNumber })
                + StringByteHelper.convertBytesToHex(buf));

        StringByteHelper.reverse(buf);

        LOGGER.debug(LOG_BUFFER_VALUE_MESSAGE_STRING, buf);

        BigInteger bigInt = new BigInteger(StringByteHelper.convertBytesToHex(buf), 16);

        LOGGER.debug(LOG_DECODED_VALUE_MESSAGE_STRING, BigInteger.class.getSimpleName(), bigInt);

        clValue.setValue(bigInt);
    }

    /**
     * Reads a {@link CLValueString} value from buffer
     * 
     * @param clValue target CLValue
     * @throws IOException
     * @throws CLValueDecodeException
     */
    public void readString(CLValueString clValue) throws IOException, CLValueDecodeException {
        int numberByteLength = 4;
        byte[] bufLength = new byte[numberByteLength];

        int readBytes = 0;
        if ((readBytes = this.read(bufLength)) != numberByteLength) {
            throwReadBytesError(Integer.class.getSimpleName(), numberByteLength, readBytes);
        }

        clValue.setBytes(StringByteHelper.convertBytesToHex(bufLength));

        int length = 0;
        for (int i = 0; i < numberByteLength; i++) {
            length += (bufLength[i] & 0xFF) << (8 * i);
        }

        LOGGER.debug("Reading string of length: {}", length);

        byte[] bufString = new byte[length];

        if ((readBytes = this.read(bufString)) != length) {
            throwReadBytesError(String.class.getSimpleName(), length, readBytes);
        }

        LOGGER.debug(LOG_BUFFER_VALUE_MESSAGE_STRING, bufString);

        String string = new String(bufString);

        LOGGER.debug(LOG_DECODED_VALUE_MESSAGE_STRING, String.class.getSimpleName(), string);

        clValue.setBytes(clValue.getBytes() + StringByteHelper.convertBytesToHex(bufString));
        clValue.setValue(string);
    }

    public void readPublicKey(CLValuePublicKey clvalue) throws NoSuchAlgorithmException, InvalidByteStringException {
        byte[] key = this.readAllBytes();
        clvalue.setBytes(StringByteHelper.convertBytesToHex(key));
        clvalue.setValue(PublicKey.fromTaggedHexString(clvalue.getBytes()));
    }

    public void readKey(CLValueKey clvalue) throws NoSuchKeyTagException, InvalidByteStringException {
        byte[] key = this.readAllBytes();
        clvalue.setBytes(StringByteHelper.convertBytesToHex(key));
        clvalue.setValue(Key.fromTaggedHexString(clvalue.getBytes()));
    }

    /**
     * Reads all bytes as a generic {@link Object}
     * 
     * @return the generic Object
     * @throws IOException
     * @throws CLValueDecodeException
     * @throws ClassNotFoundException
     */
    public void readAny(CLValueAny clValue) throws IOException, CLValueDecodeException {
        try (ObjectInputStream ois = new ObjectInputStream(this)) {
            Object obj = ois.readObject();
            clValue.setValue(obj);
        } catch (ClassNotFoundException e) {
            throw new CLValueDecodeException("Class not found", e);
        }
    }

    /**
     * Reads a single byte
     * 
     * @return byte read
     * @throws CLValueDecodeException
     * @throws IOException
     */
    protected byte readByte() throws CLValueDecodeException, IOException {
        return this.readBytes(1)[0];
    }

    /**
     * Reads {@param length} bytes
     * 
     * @param length
     * @return bytes read
     * @throws CLValueDecodeException
     * @throws IOException
     */
    protected byte[] readBytes(int length) throws CLValueDecodeException, IOException {
        byte[] buf = new byte[length];

        int readBytes = 0;
        if ((readBytes = this.read(buf)) != length) {
            throwReadBytesError(Byte.class.getSimpleName(), length, readBytes);
        }

        LOGGER.debug(LOG_BUFFER_VALUE_MESSAGE_STRING, buf);

        return buf;
    }

    private void throwReadBytesError(String simpleName, int lengthOfNextNumber, int readBytes) throws CLValueDecodeException {
        if (this.buf.length == 0) {
            throw new BufferEndCLValueDecodeException(DECODE_EXCEPTION_BUFFER_END_EMPTY_MESSAGE_STRING);
        } else if (this.buf.length > 0 && readBytes == -1) {
            throw new BufferEndCLValueDecodeException(DECODE_EXCEPTION_BUFFER_END_MESSAGE_STRING);
        } else {
            throw new CLValueDecodeException(String.format(DECODE_EXCEPTION_WRONG_LENGHT_MESSAGE_STRING, simpleName,
                    lengthOfNextNumber, readBytes));
        }
    }
}
