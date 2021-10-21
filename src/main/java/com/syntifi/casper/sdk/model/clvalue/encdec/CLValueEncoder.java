package com.syntifi.casper.sdk.model.clvalue.encdec;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.Arrays;

import com.syntifi.casper.sdk.exception.CLValueEncodeException;
import com.syntifi.casper.sdk.exception.NoSuchTypeException;
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Casper CLValue Encoding methods
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @see AbstractCLValue
 * @since 0.0.1
 */
public class CLValueEncoder extends ByteArrayOutputStream {

    private static final Logger LOGGER = LoggerFactory.getLogger(CLValueEncoder.class);

    public static final BigInteger ZERO = new BigInteger("0", 10);
    public static final BigInteger ONE = new BigInteger("1", 10);
    public static final BigInteger TWO = new BigInteger("2", 10);
    public static final BigInteger MAX_U64 = TWO.pow(64).subtract(ONE);
    public static final BigInteger MAX_U128 = TWO.pow(128).subtract(ONE);
    public static final BigInteger MAX_U256 = TWO.pow(256).subtract(ONE);
    public static final BigInteger MAX_U512 = TWO.pow(512).subtract(ONE);

    private static final String LOG_BUFFER_WRITE_TYPE_VALUE_MESSAGE_STRING = "Writing CLType {} from Java Type {}: {}";
    private static final String ENCODE_EXCEPTION_OUT_OF_BOUNDS_MESSAGE_STRING = "Value %s out of bounds for expected type %s";

    /**
     * Initializes buffer as zero-length
     */
    public CLValueEncoder() {
        super(0);
    }

    /**
     * Writes a boolean value to the CLValue byte buffer
     * 
     * @param clValue {@link CLValueBool} value to encode
     * @throws IOException
     */
    public void writeBool(CLValueBool clValue) throws IOException {
        LOGGER.debug(LOG_BUFFER_WRITE_TYPE_VALUE_MESSAGE_STRING, CLTypeData.BOOL, Boolean.class.getSimpleName(),
                clValue.getValue());

        ByteBuffer boolByteBuffer = ByteBuffer.allocate(1)
                .put(Boolean.TRUE.equals(clValue.getValue()) ? (byte) 0x01 : (byte) 0x00);

        byte[] boolBytes = boolByteBuffer.array();

        this.write(boolBytes);

        clValue.setBytes(StringByteHelper.convertBytesToHex(boolBytes));
    }

    /**
     * Writes a single byte value to the CLValue byte buffer
     * 
     * @param clValue {@link CLValueU8} value to encode
     */
    public void writeU8(CLValueU8 clValue) {
        LOGGER.debug(LOG_BUFFER_WRITE_TYPE_VALUE_MESSAGE_STRING, CLTypeData.U8, Byte.class.getSimpleName(),
                clValue.getValue());

        this.write(clValue.getValue());

        clValue.setBytes(StringByteHelper.convertBytesToHex(new byte[] { clValue.getValue().byteValue() }));
    }

    /**
     * Writes a byte array value to the CLValue byte buffer
     * 
     * @param clValue {@link CLValueByteArray} value to encode
     * @throws IOException
     */
    public void writeByteArray(CLValueByteArray clValue) throws IOException {
        LOGGER.debug(LOG_BUFFER_WRITE_TYPE_VALUE_MESSAGE_STRING, CLTypeData.BYTE_ARRAY, byte[].class.getSimpleName(),
                clValue.getValue());

        this.write(clValue.getValue());

        clValue.getClType().setLength(clValue.getValue().length);
        clValue.setBytes(StringByteHelper.convertBytesToHex(clValue.getValue()));
    }

    /**
     * Writes an Integer/I32 value to the CLValue byte buffer
     * 
     * @param clValue {@link CLValueI32} value to encode
     * @throws IOException
     */
    public void writeI32(CLValueI32 clValue) throws IOException {
        LOGGER.debug(LOG_BUFFER_WRITE_TYPE_VALUE_MESSAGE_STRING, CLTypeData.I32, Integer.class.getSimpleName(),
                clValue.getValue());

        ByteBuffer intByteBuffer = ByteBuffer.allocate(4).putInt(clValue.getValue());

        byte[] intByteArray = intByteBuffer.array();

        StringByteHelper.reverse(intByteArray);

        this.write(intByteArray);

        clValue.setBytes(StringByteHelper.convertBytesToHex(intByteArray));
    }

    /**
     * Writes an Unsigned Integer (Long)/U32 value to the CLValue byte buffer
     * 
     * @param clValue {@link CLValueU32} value to encode
     * @throws IOException
     */
    public void writeU32(CLValueU32 clValue) throws IOException {
        LOGGER.debug(LOG_BUFFER_WRITE_TYPE_VALUE_MESSAGE_STRING, CLTypeData.U32, Long.class.getSimpleName(),
                clValue.getValue());

        int unsignedInteger = clValue.getValue().intValue();

        ByteBuffer unsignedIntegerByteBuffer = ByteBuffer.allocate(4).putInt(unsignedInteger);

        byte[] unsignedIntegerByteArray = unsignedIntegerByteBuffer.array();

        StringByteHelper.reverse(unsignedIntegerByteArray);

        this.write(unsignedIntegerByteArray);

        clValue.setBytes(StringByteHelper.convertBytesToHex(unsignedIntegerByteArray));
    }

    /**
     * Writes a Long/I64 value to the CLValue byte buffer
     * 
     * @param clValue {@link CLValueI64} value to encode
     * @throws IOException
     */
    public void writeI64(CLValueI64 clValue) throws IOException {
        LOGGER.debug(LOG_BUFFER_WRITE_TYPE_VALUE_MESSAGE_STRING, CLTypeData.I64, Long.class.getSimpleName(),
                clValue.getValue());

        ByteBuffer longByteBuffer = ByteBuffer.allocate(8).putLong(clValue.getValue());

        byte[] longByteArray = longByteBuffer.array();

        StringByteHelper.reverse(longByteArray);

        this.write(longByteArray);

        clValue.setBytes(StringByteHelper.convertBytesToHex(longByteArray));
    }

    /**
     * Writes an Unsigned Long (BigInteger)/U64 to the CLValue byte buffer
     * 
     * @param clValue {@link CLValueU64} value to encode
     * @throws IOException
     * @throws CLValueEncodeException
     */
    public void writeU64(CLValueU64 clValue) throws IOException, CLValueEncodeException {
        checkBoundsFor(clValue.getValue(), CLTypeData.U64);

        LOGGER.debug(LOG_BUFFER_WRITE_TYPE_VALUE_MESSAGE_STRING, CLTypeData.U64, BigInteger.class.getSimpleName(),
                clValue.getValue());

        ByteBuffer unsignedLongByteBuffer = ByteBuffer.allocate(8).putLong(clValue.getValue().longValue());

        byte[] unsignedLongByteArray = unsignedLongByteBuffer.array();

        StringByteHelper.reverse(unsignedLongByteArray);

        this.write(unsignedLongByteArray);

        clValue.setBytes(StringByteHelper.convertBytesToHex(unsignedLongByteArray));
    }

    /**
     * Writes a BigInteger/U128 to the CLValue byte buffer
     * 
     * @param clValue {@link CLValueU128} value to encode
     * @throws IOException
     * @throws CLValueEncodeException
     */
    public void writeU128(CLValueU128 clValue) throws IOException, CLValueEncodeException {
        writeBigInteger(clValue, CLTypeData.U128);
    }

    /**
     * Writes a BigInteger/U256 to the CLValue byte buffer
     * 
     * @param clValue {@link CLValueU256} value to encode
     * @throws IOException
     * @throws CLValueEncodeException
     */
    public void writeU256(CLValueU256 clValue) throws IOException, CLValueEncodeException {
        writeBigInteger(clValue, CLTypeData.U256);
    }

    /**
     * Writes a BigInteger/U512 to the CLValue byte buffer
     * 
     * @param clValue {@link CLValueU512} value to encode
     * @throws IOException
     * @throws CLValueEncodeException
     */
    public void writeU512(CLValueU512 clValue) throws IOException, CLValueEncodeException {
        writeBigInteger(clValue, CLTypeData.U512);
    }

    /**
     * Writes a BigInteger/U128-U256-U512 to the CLValue byte buffer
     * 
     * @param clValue {@link AbstractCLValue} value to encode
     * @param clValue {@link CLTypeData} CLTypeData of BigInteger
     * @throws IOException
     * @throws CLValueEncodeException
     */
    protected void writeBigInteger(AbstractCLValue<BigInteger, ?> clValue, CLTypeData type)
            throws IOException, CLValueEncodeException {
        checkBoundsFor(clValue.getValue(), type);

        LOGGER.debug(LOG_BUFFER_WRITE_TYPE_VALUE_MESSAGE_STRING, type.getClTypeName(), BigInteger.class.getSimpleName(),
                clValue.getValue());

        byte bigIntegerLength = (byte) (Math.ceil(clValue.getValue().bitLength() / 8.0));

        this.write(bigIntegerLength);

        byte[] byteArray = clValue.getValue().toByteArray();

        // Removing leading zeroes
        int i = 0;
        boolean both = false;
        while (byteArray[i] == 0) {
            if (both) {
                i++;
            }
            both = !both;
        }

        byte[] valueByteArray = Arrays.copyOfRange(byteArray, i, bigIntegerLength + i);

        StringByteHelper.reverse(valueByteArray);

        ByteBuffer valueByteBuffer = ByteBuffer.allocate(bigIntegerLength).put(valueByteArray);

        byte[] bigIntegerBytes = valueByteBuffer.array();

        this.write(bigIntegerBytes);

        clValue.setBytes(StringByteHelper.convertBytesToHex(new byte[] { bigIntegerLength })
                + StringByteHelper.convertBytesToHex(bigIntegerBytes));
    }

    /**
     * Writes a String/String to the CLValue byte buffer
     * 
     * @param clValue {@link CLValueString} value to encode
     * @throws IOException
     */
    public void writeString(CLValueString clValue) throws IOException {
        LOGGER.debug(LOG_BUFFER_WRITE_TYPE_VALUE_MESSAGE_STRING, CLTypeData.STRING, String.class.getSimpleName(),
                clValue.getValue());

        ByteBuffer intByteBuffer = ByteBuffer.allocate(4).putInt(clValue.getValue().length());

        byte[] intByteArray = intByteBuffer.array();

        StringByteHelper.reverse(intByteArray);

        this.write(intByteArray);

        byte[] stringBytes = clValue.getValue().getBytes();

        this.write(stringBytes);

        clValue.setBytes(
                StringByteHelper.convertBytesToHex(intByteArray) + StringByteHelper.convertBytesToHex(stringBytes));
    }

    /**
     * Writes a AlgorithmTag/Key Hex string to CLValue byte buffer
     * 
     * @param clValue {@link CLValuePublicKey} value to encode
     */
    public void writePublicKey(CLValuePublicKey clValue) {
        clValue.setBytes(StringByteHelper.convertBytesToHex(new byte[] { clValue.getValue().getTag().getByteTag() })
                + StringByteHelper.convertBytesToHex(clValue.getValue().getKey()));
    }

    /**
     * Writes a Tag/Key Hex string to CLValue byte buffer
     * 
     * @param clValue {@link CLValueKey} value to encode
     */
    public void writeKey(CLValueKey clValue) {
        clValue.setBytes(StringByteHelper.convertBytesToHex(new byte[] { clValue.getValue().getTag().getByteTag() })
                + StringByteHelper.convertBytesToHex(clValue.getValue().getKey()));
    }

    public void writeAny(CLValueAny clValue) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(this)) {
            oos.writeObject(clValue.getValue());
        }

        clValue.setBytes(StringByteHelper.convertBytesToHex(this.toByteArray()));
    }

    /**
     * Checks if the value is within valid bounds for given CLType
     * 
     * @param value the value to check
     * @param type  the cltype to check against
     * @throws CLValueEncodeException
     */
    private void checkBoundsFor(BigInteger value, CLTypeData type) throws CLValueEncodeException {
        BigInteger max;
        if (type.equals(CLTypeData.U64)) {
            max = MAX_U64;
        } else if (type.equals(CLTypeData.U128)) {
            max = MAX_U128;
        } else if (type.equals(CLTypeData.U256)) {
            max = MAX_U256;
        } else if (type.equals(CLTypeData.U512)) {
            max = MAX_U512;
        } else {
            throw new CLValueEncodeException("Error checking numeric bounds", new NoSuchTypeException(
                    String.format("%s is not a numeric type with check bounds for encoding", type.getClTypeName())));
        }

        if (value.compareTo(max) > 0 || value.compareTo(ZERO) < 0) {
            throw new CLValueEncodeException(String.format(ENCODE_EXCEPTION_OUT_OF_BOUNDS_MESSAGE_STRING,
                    value.toString(), type.getClTypeName()));
        }
    }
}
