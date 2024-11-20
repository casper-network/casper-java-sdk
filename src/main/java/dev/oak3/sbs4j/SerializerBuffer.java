package dev.oak3.sbs4j;

import dev.oak3.sbs4j.exception.NoSuchTypeException;
import dev.oak3.sbs4j.exception.ValueSerializationException;
import dev.oak3.sbs4j.util.ByteUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static java.util.Objects.requireNonNull;

/**
 * Serializing methods
 *
 * @since 0.1.0
 */
public class SerializerBuffer {

    private static final Logger LOGGER = LoggerFactory.getLogger(SerializerBuffer.class);

    private ByteBuffer buffer;

    private static final int INITIAL_CAPACITY = 4096;

    public static final BigInteger ZERO = new BigInteger("0", 10);
    public static final BigInteger ONE = new BigInteger("1", 10);
    public static final BigInteger TWO = new BigInteger("2", 10);
    public static final BigInteger MAX_U64 = TWO.pow(64).subtract(ONE);
    public static final BigInteger MAX_U128 = TWO.pow(128).subtract(ONE);
    public static final BigInteger MAX_U256 = TWO.pow(256).subtract(ONE);
    public static final BigInteger MAX_U512 = TWO.pow(512).subtract(ONE);

    private static final String LOG_BUFFER_WRITE_TYPE_VALUE_MESSAGE_STRING = "Writing type {} with value: {}";
    private static final String SERIALIZE_EXCEPTION_OUT_OF_BOUNDS_MESSAGE_STRING = "Value %s out of bounds for expected type %s";

    /**
     * Initializes buffer with initial capacity of {@link #INITIAL_CAPACITY} in bytes and {@link ByteOrder#LITTLE_ENDIAN}
     */
    public SerializerBuffer() {
        this(INITIAL_CAPACITY, ByteOrder.LITTLE_ENDIAN);
    }

    /**
     * Initializes buffer with initial capacity of {@link #INITIAL_CAPACITY} and given byte order.
     *
     * @param byteOrder the byte order to be using
     */
    public SerializerBuffer(final ByteOrder byteOrder) {
        this(INITIAL_CAPACITY, byteOrder);
    }

    /**
     * Initializes buffer with given initial capacity in bytes and byte order.
     *
     * @param initialCapacity the initial capacity of the buffer
     * @param byteOrder       the byte order to be using
     */
    public SerializerBuffer(final int initialCapacity, final ByteOrder byteOrder) {
        this.buffer = ByteBuffer.allocate(initialCapacity);
        this.buffer.order(byteOrder);
        this.buffer.mark();
    }

    /**
     * Writes a boolean value to the value byte buffer
     *
     * @param value boolean value to serialize
     */
    public void writeBool(final boolean value) {
        LOGGER.debug(LOG_BUFFER_WRITE_TYPE_VALUE_MESSAGE_STRING, Boolean.class.getSimpleName(),
                value);

        byte boolByte = Boolean.TRUE.equals(value) ? (byte) 0x01 : (byte) 0x00;

        put(boolByte);
    }

    /**
     * Writes a single byte value
     *
     * @param value byte value to serialize
     */
    public void writeU8(final byte value) {
        LOGGER.debug(LOG_BUFFER_WRITE_TYPE_VALUE_MESSAGE_STRING, Byte.class.getSimpleName(), value);

        put(value);
    }

    /**
     * Writes a single byte value
     *
     * @param value byte value to serialize
     */
    public void writeU16(final short value) {
        LOGGER.debug(LOG_BUFFER_WRITE_TYPE_VALUE_MESSAGE_STRING, Short.class.getSimpleName(), value);

        put(value);
    }


    /**
     * Writes a byte array value
     *
     * @param value byte array value to serialize
     */
    public void writeByteArray(final byte[] value) {
        LOGGER.debug(LOG_BUFFER_WRITE_TYPE_VALUE_MESSAGE_STRING, byte[].class.getSimpleName(), value);

        put(value);
    }

    /**
     * Writes a Float/F32 value
     *
     * @param value F32 value to serialize
     */
    public void writeF32(final float value) {
        LOGGER.debug(LOG_BUFFER_WRITE_TYPE_VALUE_MESSAGE_STRING, Float.class.getSimpleName(), value);

        put(value);
    }

    /**
     * Writes a Double/F64 value
     *
     * @param value F64 value to serialize
     */
    public void writeF64(final double value) {
        LOGGER.debug(LOG_BUFFER_WRITE_TYPE_VALUE_MESSAGE_STRING, Double.class.getSimpleName(), value);

        put(value);
    }

    /**
     * Writes an Integer/I32 value
     *
     * @param value I32 value to serialize
     */
    public void writeI32(final int value) {
        LOGGER.debug(LOG_BUFFER_WRITE_TYPE_VALUE_MESSAGE_STRING, Integer.class.getSimpleName(), value);

        put(value);
    }

    /**
     * Writes an Unsigned Integer (Long)/U32
     *
     * @param value U32 value to serialize
     */
    public void writeU32(final Long value) {
        LOGGER.debug(LOG_BUFFER_WRITE_TYPE_VALUE_MESSAGE_STRING, Integer.class.getSimpleName(), value);

        put(value.intValue());
    }

    /**
     * Writes a Long/I64 value
     *
     * @param value I64 value to serialize
     */
    public void writeI64(final long value) {
        LOGGER.debug(LOG_BUFFER_WRITE_TYPE_VALUE_MESSAGE_STRING, Long.class.getSimpleName(), value);

        put(value);
    }

    /**
     * Writes an Unsigned Long (BigInteger)/U64 to the value byte buffer
     *
     * @param value U64 value to serialize
     *              error with input/output while reading the byte array
     * @throws ValueSerializationException exception holding information of failure to serialize a value
     */
    public void writeU64(final BigInteger value) throws ValueSerializationException {
        requireNonNull(value);
        checkBoundsFor(value, 64);

        LOGGER.debug(LOG_BUFFER_WRITE_TYPE_VALUE_MESSAGE_STRING, BigInteger.class.getSimpleName(),
                value);

        put(value.longValue());
    }

    /**
     * Writes a BigInteger/U128 to the value byte buffer
     *
     * @param value U128 value to serialize
     *              error with input/output while reading the byte array
     * @throws ValueSerializationException exception holding information of failure to serialize a value
     */
    public void writeU128(final BigInteger value) throws ValueSerializationException {
        writeBigInteger(value, 128);
    }

    /**
     * Writes a BigInteger/U256 to the value byte buffer
     *
     * @param value U256 value to serialize
     *              error with input/output while reading the byte array
     * @throws ValueSerializationException exception holding information of failure to serialize a value
     */
    public void writeU256(final BigInteger value) throws ValueSerializationException {
        writeBigInteger(value, 256);
    }

    /**
     * Writes a BigInteger/U512 to the value byte buffer
     *
     * @param value U512 value to serialize
     *              error with input/output while reading the byte array
     * @throws ValueSerializationException exception holding information of failure to serialize a value
     */
    public void writeU512(final BigInteger value) throws ValueSerializationException {
        writeBigInteger(value, 512);
    }

    /**
     * Writes a BigInteger/U128-U256-U512 to the value byte buffer
     *
     * @param value BigInteger to serialize
     * @param size  the bit size of BigInteger
     *              error with input/output while reading the byte array
     * @throws ValueSerializationException exception holding information of failure to serialize a value
     */
    protected void writeBigInteger(final BigInteger value, final int size) throws ValueSerializationException {
        requireNonNull(value);
        checkBoundsFor(value, size);

        LOGGER.debug(LOG_BUFFER_WRITE_TYPE_VALUE_MESSAGE_STRING, BigInteger.class.getSimpleName(), value);

        final byte bigIntegerLength = (byte) (Math.ceil(value.bitLength() / 8.0));

        final byte[] byteArray = value.toByteArray();

        int skipped = 0;
        boolean skip = true;
        for (byte b : byteArray) {
            boolean signByte = b == (byte) 0x00;
            if (skip && signByte) {
                skipped++;
            } else if (skip) {
                skip = false;
            }
        }

        final byte[] bigIntegerBytes = Arrays.copyOfRange(byteArray, skipped, byteArray.length);

        if (this.buffer.order() == ByteOrder.LITTLE_ENDIAN) {
            ByteUtils.reverse(bigIntegerBytes);
        }

        put(bigIntegerLength);
        put(bigIntegerBytes);
    }

    /**
     * Writes a String to the value byte buffer
     *
     * @param value String value to serialize
     *              error with input/output while reading the byte array
     */
    public void writeString(final String value) {
        LOGGER.debug(LOG_BUFFER_WRITE_TYPE_VALUE_MESSAGE_STRING, String.class.getSimpleName(), value);

        put(value.getBytes(StandardCharsets.UTF_8).length);
        put(value.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Checks if the value is within valid bounds for given CLType
     *
     * @param value the value to check
     * @param size  the bit size to check against
     * @throws ValueSerializationException exception holding information of failure to serialize a value
     */
    private void checkBoundsFor(final BigInteger value, final int size) throws ValueSerializationException {
        final BigInteger max;
        if (size == 64) {
            max = MAX_U64;
        } else if (size == 128) {
            max = MAX_U128;
        } else if (size == 256) {
            max = MAX_U256;
        } else if (size == 512) {
            max = MAX_U512;
        } else {
            throw new ValueSerializationException("Error checking numeric bounds", new NoSuchTypeException(
                    String.format("%s is not a numeric size with check bounds for serializing", size)));
        }

        if (value.compareTo(max) > 0 || value.compareTo(ZERO) < 0) {
            throw new ValueSerializationException(String.format(SERIALIZE_EXCEPTION_OUT_OF_BOUNDS_MESSAGE_STRING,
                    value, size));
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
     * Gets the full byte array corresponding to serialized data
     *
     * @return the byte array serialized data
     */
    public byte[] toByteArray() {
        return Arrays.copyOfRange(
                this.buffer.array(),
                this.buffer.arrayOffset(), this.buffer.arrayOffset() + this.buffer.position()
        );
    }

    private void put(final byte newByte) {
        // check if it fits and increase buffer if needed
        if (this.buffer.position() + 1 >= this.buffer.capacity()) {
            increaseCapacity(1);
        }
        this.buffer.put(newByte);
    }

    private void put(final short bytes) {
        // check if it fits and increase buffer if needed
        if (this.buffer.position() + 2 >= this.buffer.capacity()) {
            increaseCapacity(2);
        }
        this.buffer.putShort(bytes);
    }


    private void put(final byte[] newBytes) {
        // check if it fits and increase buffer if needed
        if (this.buffer.position() + newBytes.length >= this.buffer.capacity()) {
            increaseCapacity(newBytes.length);
        }
        this.buffer.put(newBytes);
    }

    private void put(final float bytes) {
        // check if it fits and increase buffer if needed
        if (this.buffer.position() + 4 >= this.buffer.capacity()) {
            increaseCapacity(4);
        }
        this.buffer.putFloat(bytes);
    }

    private void put(final double bytes) {
        // check if it fits and increase buffer if needed
        if (this.buffer.position() + 8 >= this.buffer.capacity()) {
            increaseCapacity(8);
        }
        this.buffer.putDouble(bytes);
    }

    private void put(final int bytes) {
        // check if it fits and increase buffer if needed
        if (this.buffer.position() + 4 >= this.buffer.capacity()) {
            increaseCapacity(4);
        }
        this.buffer.putInt(bytes);
    }

    private void put(final long bytes) {
        // check if it fits and increase buffer if needed
        if (this.buffer.position() + 8 >= this.buffer.capacity()) {
            increaseCapacity(8);
        }
        this.buffer.putLong(bytes);
    }

    /**
     * Increases the buffer size and replaces with the new capacity buffer including current buffer data
     *
     * @param increaseByteCount the byte count to increase
     * @throws IllegalArgumentException if the parameter is invalid
     */
    protected void increaseCapacity(final int increaseByteCount) throws IllegalArgumentException {
        if (this.buffer == null) {
            throw new IllegalArgumentException("Buffer is null");
        }

        if (increaseByteCount < 0) {
            throw new IllegalArgumentException("Size cannot be less than 0");
        }

        int newCapacity = this.buffer.capacity() + increaseByteCount;
        ByteBuffer newBuffer = ByteBuffer.allocate(newCapacity);
        this.buffer.flip();
        newBuffer.order(this.buffer.order());
        newBuffer.put(this.buffer);
        this.buffer = newBuffer;
    }
}
