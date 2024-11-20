package dev.oak3.sbs4j;

import dev.oak3.sbs4j.exception.ValueDeserializationException;
import dev.oak3.sbs4j.util.ByteUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * Deserializing methods
 *
 * @since 0.1.0
 */
public class DeserializerBuffer {

    private final ByteBuffer buffer;

    private static final Logger LOGGER = LoggerFactory.getLogger(DeserializerBuffer.class);

    private static final String LOG_BUFFER_INIT_MESSAGE_HEX_STRING = "Initializing DeserializerBuffer with hexString: {} and byte order {}";
    private static final String LOG_BUFFER_INIT_MESSAGE = "Initializing DeserializerBuffer with bytes: {} and byte order {}";
    private static final String LOG_BUFFER_VALUE_MESSAGE_STRING = "Buffer value: {}";
    private static final String LOG_SERIALIZED_VALUE_MESSAGE_STRING = "Deserialized value for {}: {}";
    private static final String SERIALIZE_EXCEPTION_OUT_OF_BOUNDS_MESSAGE_STRING = "Value %s out of bounds for expected type %s";

    /**
     * Initializes buffer with serialized bytes from hex-encoded {@link String}
     *
     * @param hexString hex-encoded {@link String} to deserialize and read from
     */
    public DeserializerBuffer(final String hexString) {
        this(!hexString.isEmpty() ? ByteUtils.parseHexString(hexString) : new byte[]{});
    }

    /**
     * Initializes buffer with serialized bytes from hex-encoded {@link String}
     *
     * @param hexString hex-encoded {@link String} to deserialize and read from
     * @param byteOrder the byte order to be using
     */
    public DeserializerBuffer(final String hexString, final ByteOrder byteOrder) {
        this(!hexString.isEmpty() ? ByteUtils.parseHexString(hexString) : new byte[]{}, byteOrder);

        LOGGER.debug(LOG_BUFFER_INIT_MESSAGE_HEX_STRING, hexString, byteOrder);
    }

    /**
     * Initializes buffer with serialized bytes and {@link ByteOrder#LITTLE_ENDIAN}
     *
     * @param bytes byte array to deserialize and read from
     */
    public DeserializerBuffer(final byte[] bytes) {
        this(bytes, ByteOrder.LITTLE_ENDIAN);
    }

    /**
     * Initializes buffer with serialized bytes and byte order
     *
     * @param bytes     byte array to deserialize and read from
     * @param byteOrder the byte order to be using
     */
    public DeserializerBuffer(final byte[] bytes, final ByteOrder byteOrder) {
        this.buffer = ByteBuffer.wrap(bytes);
        this.buffer.order(byteOrder);
        this.buffer.mark();

        LOGGER.debug(LOG_BUFFER_INIT_MESSAGE, Arrays.toString(bytes), byteOrder);
    }

    /**
     * Reads a Boolean value
     *
     * @return true if 1, while false if 0
     * @throws ValueDeserializationException exception holding information of failure to deserialize a value
     */
    public Boolean readBool() throws ValueDeserializationException {
        try {
            final byte buf = this.buffer.get();

            LOGGER.debug(LOG_BUFFER_VALUE_MESSAGE_STRING, buf);

            if (buf == 1) {
                return true;
            } else if (buf == 0) {
                return false;
            } else {
                throw new ValueDeserializationException(
                        String.format(SERIALIZE_EXCEPTION_OUT_OF_BOUNDS_MESSAGE_STRING, buf, Boolean.class.getSimpleName()));
            }
        } catch (BufferUnderflowException bufferUnderflowException) {
            throw new ValueDeserializationException("Error while reading boolean from buffer", bufferUnderflowException);
        }
    }

    /**
     * Reads a byte from buffer
     *
     * @return the byte
     */
    public byte readU8() throws ValueDeserializationException {
        try {
            final byte u8 = this.buffer.get();

            LOGGER.debug(LOG_SERIALIZED_VALUE_MESSAGE_STRING, Byte.class.getSimpleName(), u8);

            return u8;
        } catch (BufferUnderflowException bufferUnderflowException) {
            throw new ValueDeserializationException("Error while reading U8 from buffer", bufferUnderflowException);
        }
    }

    /**
     * Reads a byte from buffer
     *
     * @return the byte
     */
    public short readU16() throws ValueDeserializationException {
        try {
            final short u16 = this.buffer.getShort();

            LOGGER.debug(LOG_SERIALIZED_VALUE_MESSAGE_STRING, Short.class.getSimpleName(), u16);

            return u16;
        } catch (BufferUnderflowException bufferUnderflowException) {
            throw new ValueDeserializationException("Error while reading U16 from buffer", bufferUnderflowException);
        }
    }


    /**
     * Reads a byte[] from buffer
     *
     * @param length the length of the array
     * @return the byte array as byte[]
     */
    public byte[] readByteArray(final int length) throws ValueDeserializationException {
        try {
            final byte[] bytes = readBytes(length);

            LOGGER.debug(LOG_SERIALIZED_VALUE_MESSAGE_STRING, Byte.class.getSimpleName(), bytes);

            return bytes;
        } catch (BufferUnderflowException bufferUnderflowException) {
            throw new ValueDeserializationException("Error while reading byte array from buffer", bufferUnderflowException);
        }
    }

    /**
     * Reads a float of 32 bits (4 bytes)
     *
     * @return the number as a float
     */
    public float readF32() throws ValueDeserializationException {
        try {
            final float floatNumber = this.buffer.getFloat();

            LOGGER.debug(LOG_SERIALIZED_VALUE_MESSAGE_STRING, Float.class.getSimpleName(), floatNumber);

            return floatNumber;
        } catch (BufferUnderflowException bufferUnderflowException) {
            throw new ValueDeserializationException("Error while reading F32 from buffer", bufferUnderflowException);
        }
    }

    /**
     * Reads a float of 64 bits (8 bytes)
     *
     * @return the number as a double
     */
    public double readF64() throws ValueDeserializationException {
        try {
            final double doubleNumber = this.buffer.getDouble();

            LOGGER.debug(LOG_SERIALIZED_VALUE_MESSAGE_STRING, Double.class.getSimpleName(), doubleNumber);

            return doubleNumber;
        } catch (BufferUnderflowException bufferUnderflowException) {
            throw new ValueDeserializationException("Error while reading F64 from buffer", bufferUnderflowException);
        }
    }

    /**
     * Reads a signed int of 32 bits (4 bytes)
     *
     * @return the number as an int
     */
    public int readI32() throws ValueDeserializationException {
        try {
            final int integerNumber = this.buffer.getInt();

            LOGGER.debug(LOG_SERIALIZED_VALUE_MESSAGE_STRING, Long.class.getSimpleName(), integerNumber);

            return integerNumber;
        } catch (BufferUnderflowException bufferUnderflowException) {
            throw new ValueDeserializationException("Error while reading I32 from buffer", bufferUnderflowException);
        }
    }

    /**
     * Reads an unsigned int of 32 bits (4 bytes)
     *
     * @return the number as a long
     */
    public long readU32() throws ValueDeserializationException {
        try {
            final int signedInteger = this.buffer.getInt();
            final long unsignedIntegerLong = Integer.toUnsignedLong(signedInteger);

            LOGGER.debug(LOG_SERIALIZED_VALUE_MESSAGE_STRING, Long.class.getSimpleName(), unsignedIntegerLong);
            return unsignedIntegerLong;
        } catch (BufferUnderflowException bufferUnderflowException) {
            throw new ValueDeserializationException("Error while reading U32 from buffer", bufferUnderflowException);
        }
    }

    /**
     * Reads a signed int of 64 bits (8 bytes)
     *
     * @return the number as a long
     */
    public long readI64() throws ValueDeserializationException {
        try {
            final long longNumber = this.buffer.getLong();

            LOGGER.debug(LOG_SERIALIZED_VALUE_MESSAGE_STRING, Long.class.getSimpleName(), longNumber);

            return longNumber;
        } catch (BufferUnderflowException bufferUnderflowException) {
            throw new ValueDeserializationException("Error while reading I64 from buffer", bufferUnderflowException);
        }
    }

    /**
     * Reads an unsigned int of 64 bits (8 bytes)
     *
     * @return the number as a BigInteger
     */
    public BigInteger readU64() throws ValueDeserializationException {
        try {
            // Since this is a positive (unsigned) number, we should prefix with a zero
            // byte to parse correctly
            final ByteBuffer bb = ByteBuffer.allocate(9);
            bb.put((byte) 0);
            bb.putLong(this.buffer.getLong());
            final BigInteger unsignedLong = new BigInteger(bb.array());

            LOGGER.debug(LOG_SERIALIZED_VALUE_MESSAGE_STRING, BigInteger.class.getSimpleName(), unsignedLong);

            return unsignedLong;
        } catch (BufferUnderflowException bufferUnderflowException) {
            throw new ValueDeserializationException("Error while reading U64 from buffer", bufferUnderflowException);
        }
    }

    /**
     * Reads an unsigned int of 128 bits (16 bytes) max
     *
     * @return the number as a BigInteger
     */
    public BigInteger readU128() throws ValueDeserializationException {
        try {
            return this.readBigInteger();
        } catch (BufferUnderflowException bufferUnderflowException) {
            throw new ValueDeserializationException("Error while reading U128 from buffer", bufferUnderflowException);
        }
    }

    /**
     * Reads U256 from buffer
     *
     * @return the number as a BigInteger
     */
    public BigInteger readU256() throws ValueDeserializationException {
        try {
            return this.readBigInteger();
        } catch (BufferUnderflowException bufferUnderflowException) {
            throw new ValueDeserializationException("Error while reading U256 from buffer", bufferUnderflowException);
        }
    }

    /**
     * Reads U512 from buffer
     *
     * @return the number as a BigInteger
     */
    public BigInteger readU512() throws ValueDeserializationException {
        try {
            return this.readBigInteger();
        } catch (BufferUnderflowException bufferUnderflowException) {
            throw new ValueDeserializationException("Error while reading U512 from buffer", bufferUnderflowException);
        }
    }

    /**
     * Larger numeric values (e.g., U128, U256, U512) serialize as one byte of the
     * length of the next number, followed by the two’s complement representation
     * with little-endian byte order.
     *
     * @return the number as a BigInteger
     */
    protected BigInteger readBigInteger() {
        final byte lengthOfNextNumber = this.buffer.get();

        LOGGER.debug("Length of next number: {}", lengthOfNextNumber);

        final byte[] buf = new byte[lengthOfNextNumber + 1];

        this.buffer.get(buf, 1, lengthOfNextNumber);

        LOGGER.debug(LOG_BUFFER_VALUE_MESSAGE_STRING, buf);

        if (this.buffer.order() == ByteOrder.LITTLE_ENDIAN) {
            ByteUtils.reverse(buf, 1, buf.length - 1);
        }

        final BigInteger bigInt = new BigInteger(buf);

        LOGGER.debug(LOG_SERIALIZED_VALUE_MESSAGE_STRING, BigInteger.class.getSimpleName(), bigInt);

        return bigInt;
    }

    /**
     * Reads a String value from buffer
     *
     * @return the String read
     */
    public String readString() throws ValueDeserializationException {
        try {
            final int length = this.buffer.getInt();

            LOGGER.debug("Reading string of length: {}", length);

            final byte[] bufString = new byte[length];

            this.buffer.get(bufString, 0, length);

            LOGGER.debug(LOG_BUFFER_VALUE_MESSAGE_STRING, bufString);

            final String string = new String(bufString, StandardCharsets.UTF_8);

            LOGGER.debug(LOG_SERIALIZED_VALUE_MESSAGE_STRING, String.class.getSimpleName(), string);

            return string;
        } catch (BufferUnderflowException bufferUnderflowException) {
            throw new ValueDeserializationException("Error while reading String from buffer", bufferUnderflowException);
        }
    }

    /**
     * Retrieves the backing buffer
     *
     * @return the Deserializer ByteBuffer
     */
    public ByteBuffer getBuffer() {
        return this.buffer;
    }

    /**
     * Reads a specified number of bytes
     *
     * @param length the number of bytes to read
     * @return bytes read
     */
    protected byte[] readBytes(int length) {
        final byte[] buf = new byte[length];

        this.buffer.get(buf, 0, length);

        LOGGER.debug(LOG_BUFFER_VALUE_MESSAGE_STRING, buf);

        return buf;
    }
}
