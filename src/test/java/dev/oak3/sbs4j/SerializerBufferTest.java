package dev.oak3.sbs4j;

import dev.oak3.sbs4j.exception.ValueSerializationException;
import dev.oak3.sbs4j.model.Point;
import dev.oak3.sbs4j.model.Polygon;
import dev.oak3.sbs4j.model.Sphere;
import dev.oak3.sbs4j.model.TestData;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class SerializerBufferTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(SerializerBufferTest.class);

    @Test
    void serializePoint_should_match_expected_array() {
        byte[] expectedBytes = new byte[]{4, 0, 0, 0, 2, 0, 0, 0, 9, 0, 0, 0};

        SerializerBuffer serializerBuffer = new SerializerBuffer();

        Point point = new Point(4, 2, 9);
        point.serialize(serializerBuffer);

        byte[] serializedBytes = serializerBuffer.toByteArray();

        assertArrayEquals(expectedBytes, serializedBytes);
    }

    @Test
    void serializeCircle_should_match_expected_array() {
        byte[] expectedBytes = new byte[]{4, 0, 0, 0, 2, 0, 0, 0, 9, 0, 0, 0, 0, 0, -96, 64};

        SerializerBuffer serializerBuffer = new SerializerBuffer();

        Point center = new Point(4, 2, 9);
        Sphere sphere = new Sphere(center, 5.0f);
        sphere.serialize(serializerBuffer);

        byte[] serializedBytes = serializerBuffer.toByteArray();

        assertArrayEquals(expectedBytes, serializedBytes);
    }

    @Test
    void serializePolygon_should_match_expected_array() {
        byte[] expectedBytes = new byte[]{4, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, -1, -1, -1, -1, 0, 0, 0, 0, -1, -1, -1, -1, -1, -1, -1, -1, 0, 0, 0, 0, -1, -1, -1, -1, 1, 0, 0, 0, 0, 0, 0, 0};

        SerializerBuffer serializerBuffer = new SerializerBuffer();

        Polygon square = new Polygon();

        Point p1 = new Point(1, 1, 0);
        Point p2 = new Point(1, -1, 0);
        Point p3 = new Point(-1, -1, 0);
        Point p4 = new Point(-1, 1, 0);

        square.setVertices(Arrays.asList(p1, p2, p3, p4));

        square.serialize(serializerBuffer);

        byte[] serializedBytes = serializerBuffer.toByteArray();

        assertArrayEquals(expectedBytes, serializedBytes);
    }

    @Test
    void validateSerialize_with_large_data_without_buffer_overflow() throws URISyntaxException, IOException {
        Path imagePath = Paths.get(Objects.requireNonNull(getClass().getClassLoader().getResource("test_image.png")).toURI());
        final byte[] inputBytes = Files.readAllBytes(imagePath);

        SerializerBuffer serializerBuffer = new SerializerBuffer();
        assertDoesNotThrow(() -> serializerBuffer.writeI32(1));
        assertDoesNotThrow(() -> serializerBuffer.writeF32(2.0f));
        assertDoesNotThrow(() -> serializerBuffer.writeI64(3));
        assertDoesNotThrow(() -> serializerBuffer.writeF64(2.0));
        assertDoesNotThrow(() -> serializerBuffer.writeByteArray(inputBytes));
        assertDoesNotThrow(() -> serializerBuffer.writeString("Test string"));
    }

    @Test
    void validateSerialize_with_SampleData() throws ValueSerializationException {
        for (TestData<?> testData : TestData.SUCCESS_TEST_DATA) {
            SerializerBuffer serializerBuffer = new SerializerBuffer();
            switch (testData.getName()) {
                case TestData.BOOLEAN:
                    serializerBuffer.writeBool((Boolean) testData.getValue());
                    LOGGER.debug("{} | Expected: {} | Actual {}", Boolean.class.getSimpleName(), testData.getByteValueLittleEndian(), serializerBuffer.toByteArray());
                    assertArrayEquals(testData.getByteValueLittleEndian(), serializerBuffer.toByteArray());
                    break;
                case TestData.BYTE_ARRAY:
                    serializerBuffer.writeByteArray((byte[]) testData.getValue());
                    LOGGER.debug("{} | Expected: {} | Actual {}", byte[].class.getSimpleName(), testData.getByteValueLittleEndian(), serializerBuffer.toByteArray());
                    assertArrayEquals(testData.getByteValueLittleEndian(), serializerBuffer.toByteArray());
                    break;
                case TestData.U_8:
                    serializerBuffer.writeU8((Byte) testData.getValue());
                    LOGGER.debug("{} | Expected: {} | Actual {}", Byte.class.getSimpleName(), testData.getByteValueLittleEndian(), serializerBuffer.toByteArray());
                    assertArrayEquals(testData.getByteValueLittleEndian(), serializerBuffer.toByteArray());
                    break;
                case TestData.U_16:
                    serializerBuffer.writeU16((Short) testData.getValue());
                    LOGGER.debug("{} | Expected: {} | Actual {}", Short.class.getSimpleName(), testData.getByteValueLittleEndian(), serializerBuffer.toByteArray());
                    assertArrayEquals(testData.getByteValueLittleEndian(), serializerBuffer.toByteArray());
                    break;
                case TestData.U_32:
                    serializerBuffer.writeU32((Long) testData.getValue());
                    LOGGER.debug("{} | Expected: {} | Actual {}", Long.class.getSimpleName(), testData.getByteValueLittleEndian(), serializerBuffer.toByteArray());
                    assertArrayEquals(testData.getByteValueLittleEndian(), serializerBuffer.toByteArray());
                    break;
                case TestData.F_32:
                    serializerBuffer.writeF32((Float) testData.getValue());
                    LOGGER.debug("{} | Expected: {} | Actual {}", Float.class.getSimpleName(), testData.getByteValueLittleEndian(), serializerBuffer.toByteArray());
                    assertArrayEquals(testData.getByteValueLittleEndian(), serializerBuffer.toByteArray());
                    break;
                case TestData.F_64:
                    serializerBuffer.writeF64((Double) testData.getValue());
                    LOGGER.debug("{} | Expected: {} | Actual {}", Double.class.getSimpleName(), testData.getByteValueLittleEndian(), serializerBuffer.toByteArray());
                    assertArrayEquals(testData.getByteValueLittleEndian(), serializerBuffer.toByteArray());
                    break;
                case TestData.U_64:
                    serializerBuffer.writeU64((BigInteger) testData.getValue());
                    LOGGER.debug("{} | Expected: {} | Actual {}", BigInteger.class.getSimpleName(), testData.getByteValueLittleEndian(), serializerBuffer.toByteArray());
                    assertArrayEquals(testData.getByteValueLittleEndian(), serializerBuffer.toByteArray());
                    break;
                case TestData.I_32:
                    serializerBuffer.writeI32((Integer) testData.getValue());
                    LOGGER.debug("{} | Expected: {} | Actual {}", Integer.class.getSimpleName(), testData.getByteValueLittleEndian(), serializerBuffer.toByteArray());
                    assertArrayEquals(testData.getByteValueLittleEndian(), serializerBuffer.toByteArray());
                    break;
                case TestData.I_64:
                    serializerBuffer.writeI64((Long) testData.getValue());
                    LOGGER.debug("{} | Expected: {} | Actual {}", Long.class.getSimpleName(), testData.getByteValueLittleEndian(), serializerBuffer.toByteArray());
                    assertArrayEquals(testData.getByteValueLittleEndian(), serializerBuffer.toByteArray());
                    break;
                case TestData.U_128:
                    serializerBuffer.writeU128((BigInteger) testData.getValue());
                    LOGGER.debug("{} | Expected: {} | Actual {}", BigInteger.class.getSimpleName(), testData.getByteValueLittleEndian(), serializerBuffer.toByteArray());
                    assertArrayEquals(testData.getByteValueLittleEndian(), serializerBuffer.toByteArray());
                    break;
                case TestData.U_256:
                    serializerBuffer.writeU256((BigInteger) testData.getValue());
                    LOGGER.debug("{} | Expected: {} | Actual {}", BigInteger.class.getSimpleName(), testData.getByteValueLittleEndian(), serializerBuffer.toByteArray());
                    assertArrayEquals(testData.getByteValueLittleEndian(), serializerBuffer.toByteArray());
                    break;
                case TestData.U_512:
                    serializerBuffer.writeU512((BigInteger) testData.getValue());
                    LOGGER.debug("{} | Expected: {} | Actual {}", BigInteger.class.getSimpleName(), testData.getByteValueLittleEndian(), serializerBuffer.toByteArray());
                    assertArrayEquals(testData.getByteValueLittleEndian(), serializerBuffer.toByteArray());
                    break;
                case TestData.STRING:
                    serializerBuffer.writeString((String) testData.getValue());
                    LOGGER.debug("{} | Expected: {} | Actual {}", String.class.getSimpleName(), testData.getByteValueLittleEndian(), serializerBuffer.toByteArray());
                    assertArrayEquals(testData.getByteValueLittleEndian(), serializerBuffer.toByteArray());
                    break;
            }
        }
    }

    @Test
    void validateSerialize_BIG_ENDIAN_with_SampleData() throws ValueSerializationException {
        for (TestData<?> testData : TestData.SUCCESS_TEST_DATA) {
            SerializerBuffer serializerBuffer = new SerializerBuffer(ByteOrder.BIG_ENDIAN);
            switch (testData.getName()) {
                case TestData.BOOLEAN:
                    serializerBuffer.writeBool((Boolean) testData.getValue());
                    LOGGER.debug("{} | Expected: {} | Actual {}", Boolean.class.getSimpleName(), testData.getByteValueBigEndian(), serializerBuffer.toByteArray());
                    assertArrayEquals(testData.getByteValueBigEndian(), serializerBuffer.toByteArray());
                    break;
                case TestData.BYTE_ARRAY:
                    serializerBuffer.writeByteArray((byte[]) testData.getValue());
                    LOGGER.debug("{} | Expected: {} | Actual {}", byte[].class.getSimpleName(), testData.getByteValueBigEndian(), serializerBuffer.toByteArray());
                    assertArrayEquals(testData.getByteValueBigEndian(), serializerBuffer.toByteArray());
                    break;
                case TestData.U_8:
                    serializerBuffer.writeU8((Byte) testData.getValue());
                    LOGGER.debug("{} | Expected: {} | Actual {}", Byte.class.getSimpleName(), testData.getByteValueBigEndian(), serializerBuffer.toByteArray());
                    assertArrayEquals(testData.getByteValueBigEndian(), serializerBuffer.toByteArray());
                    break;
                case TestData.U_32:
                    serializerBuffer.writeU32((Long) testData.getValue());
                    LOGGER.debug("{} | Expected: {} | Actual {}", Long.class.getSimpleName(), testData.getByteValueBigEndian(), serializerBuffer.toByteArray());
                    assertArrayEquals(testData.getByteValueBigEndian(), serializerBuffer.toByteArray());
                    break;
                case TestData.F_32:
                    serializerBuffer.writeF32((Float) testData.getValue());
                    LOGGER.debug("{} | Expected: {} | Actual {}", Float.class.getSimpleName(), testData.getByteValueBigEndian(), serializerBuffer.toByteArray());
                    assertArrayEquals(testData.getByteValueBigEndian(), serializerBuffer.toByteArray());
                    break;
                case TestData.F_64:
                    serializerBuffer.writeF64((Double) testData.getValue());
                    LOGGER.debug("{} | Expected: {} | Actual {}", Double.class.getSimpleName(), testData.getByteValueBigEndian(), serializerBuffer.toByteArray());
                    assertArrayEquals(testData.getByteValueBigEndian(), serializerBuffer.toByteArray());
                    break;
                case TestData.U_64:
                    serializerBuffer.writeU64((BigInteger) testData.getValue());
                    LOGGER.debug("{} | Expected: {} | Actual {}", BigInteger.class.getSimpleName(), testData.getByteValueBigEndian(), serializerBuffer.toByteArray());
                    assertArrayEquals(testData.getByteValueBigEndian(), serializerBuffer.toByteArray());
                    break;
                case TestData.I_32:
                    serializerBuffer.writeI32((Integer) testData.getValue());
                    LOGGER.debug("{} | Expected: {} | Actual {}", Integer.class.getSimpleName(), testData.getByteValueBigEndian(), serializerBuffer.toByteArray());
                    assertArrayEquals(testData.getByteValueBigEndian(), serializerBuffer.toByteArray());
                    break;
                case TestData.I_64:
                    serializerBuffer.writeI64((Long) testData.getValue());
                    LOGGER.debug("{} | Expected: {} | Actual {}", Long.class.getSimpleName(), testData.getByteValueBigEndian(), serializerBuffer.toByteArray());
                    assertArrayEquals(testData.getByteValueBigEndian(), serializerBuffer.toByteArray());
                    break;
                case TestData.U_128:
                    serializerBuffer.writeU128((BigInteger) testData.getValue());
                    LOGGER.debug("{} | Expected: {} | Actual {}", BigInteger.class.getSimpleName(), testData.getByteValueBigEndian(), serializerBuffer.toByteArray());
                    assertArrayEquals(testData.getByteValueBigEndian(), serializerBuffer.toByteArray());
                    break;
                case TestData.U_256:
                    serializerBuffer.writeU256((BigInteger) testData.getValue());
                    LOGGER.debug("{} | Expected: {} | Actual {}", BigInteger.class.getSimpleName(), testData.getByteValueBigEndian(), serializerBuffer.toByteArray());
                    assertArrayEquals(testData.getByteValueBigEndian(), serializerBuffer.toByteArray());
                    break;
                case TestData.U_512:
                    serializerBuffer.writeU512((BigInteger) testData.getValue());
                    LOGGER.debug("{} | Expected: {} | Actual {}", BigInteger.class.getSimpleName(), testData.getByteValueBigEndian(), serializerBuffer.toByteArray());
                    assertArrayEquals(testData.getByteValueBigEndian(), serializerBuffer.toByteArray());
                    break;
                case TestData.STRING:
                    serializerBuffer.writeString((String) testData.getValue());
                    LOGGER.debug("{} | Expected: {} | Actual {}", String.class.getSimpleName(), testData.getByteValueBigEndian(), serializerBuffer.toByteArray());
                    assertArrayEquals(testData.getByteValueBigEndian(), serializerBuffer.toByteArray());
                    break;
            }
        }
    }

    @Test
    void numbersShouldBeInsideTheirTypeBounds() throws ValueSerializationException {
        for (TestData<?> testData : TestData.LAST_VALID_NUMBER_TEST_DATA) {
            SerializerBuffer serializerBuffer = new SerializerBuffer();
            switch (testData.getName()) {
                case TestData.U_64:
                    LOGGER.debug("Testing last valid number (value: {}) for {}", testData.getValue(),
                            "U64");
                    serializerBuffer.writeU64((BigInteger) testData.getValue());
                    assertNotNull(serializerBuffer.toByteArray());
                    break;
                case TestData.U_128:
                    LOGGER.debug("Testing last valid number (value: {}) for {}", testData.getValue(),
                            "U128");
                    serializerBuffer.writeU128((BigInteger) testData.getValue());
                    assertNotNull(serializerBuffer.toByteArray());
                    break;
                case TestData.U_256:
                    LOGGER.debug("Testing last valid number (value: {}) for {}", testData.getValue(),
                            "U256");
                    serializerBuffer.writeU256((BigInteger) testData.getValue());
                    assertNotNull(serializerBuffer.toByteArray());
                    break;
                case TestData.U_512:
                    LOGGER.debug("Testing last valid number (value: {}) for {}", testData.getValue(),
                            "U512");
                    serializerBuffer.writeU512((BigInteger) testData.getValue());
                    assertNotNull(serializerBuffer.toByteArray());
                    break;
            }
        }
    }

    @Test
    void dataOutOfBounds_should_throw_ValueSerializationException() {
        for (TestData<?> testData : TestData.OUT_OF_BOUNDS_TEST_DATA) {
            SerializerBuffer serializerBuffer = new SerializerBuffer();
            switch (testData.getName()) {
                case TestData.U_64:
                    assertThrows(ValueSerializationException.class, () -> serializerBuffer.writeU64((BigInteger) testData.getValue()));
                    break;
                case TestData.U_128:
                    assertThrows(ValueSerializationException.class, () -> serializerBuffer.writeU128((BigInteger) testData.getValue()));
                    break;
                case TestData.U_256:
                    assertThrows(ValueSerializationException.class, () -> serializerBuffer.writeU256((BigInteger) testData.getValue()));
                    break;
                case TestData.U_512:
                    assertThrows(ValueSerializationException.class, () -> serializerBuffer.writeU512((BigInteger) testData.getValue()));
                    break;
            }
        }
    }
}
