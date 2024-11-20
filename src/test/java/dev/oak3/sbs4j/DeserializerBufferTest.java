package dev.oak3.sbs4j;

import dev.oak3.sbs4j.exception.ValueDeserializationException;
import dev.oak3.sbs4j.model.Point;
import dev.oak3.sbs4j.model.Polygon;
import dev.oak3.sbs4j.model.Sphere;
import dev.oak3.sbs4j.model.TestData;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.nio.ByteOrder;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class DeserializerBufferTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeserializerBufferTest.class);

    @Test
    void deserializePoint_should_match_expected_object() throws ValueDeserializationException {
        byte[] serializedBytes = new byte[]{4, 0, 0, 0, 2, 0, 0, 0, 9, 0, 0, 0};

        DeserializerBuffer deserializerBuffer = new DeserializerBuffer(serializedBytes);

        Point expected = new Point(4, 2, 9);

        Point point = new Point();
        point.deserialize(deserializerBuffer);

        assertEquals(expected, point);
    }

    @Test
    void deserializeCircle_should_match_expected_object() throws ValueDeserializationException {
        byte[] serializedBytes = new byte[]{4, 0, 0, 0, 2, 0, 0, 0, 9, 0, 0, 0, 0, 0, -96, 64};

        DeserializerBuffer deserializerBuffer = new DeserializerBuffer(serializedBytes);

        Sphere expected = new Sphere(new Point(4, 2, 9), 5.0f);

        Sphere sphere = new Sphere();
        sphere.deserialize(deserializerBuffer);

        assertEquals(expected, sphere);
    }

    @Test
    void deserializePolygon_should_match_expected_object() throws ValueDeserializationException {
        byte[] serializedBytes = new byte[]{4, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, -1, -1, -1, -1, 0, 0, 0, 0, -1, -1, -1, -1, -1, -1, -1, -1, 0, 0, 0, 0, -1, -1, -1, -1, 1, 0, 0, 0, 0, 0, 0, 0};

        DeserializerBuffer deserializerBuffer = new DeserializerBuffer(serializedBytes);

        Polygon expected = new Polygon();

        Point p1 = new Point(1, 1, 0);
        Point p2 = new Point(1, -1, 0);
        Point p3 = new Point(-1, -1, 0);
        Point p4 = new Point(-1, 1, 0);

        expected.setVertices(Arrays.asList(p1, p2, p3, p4));

        Polygon polygon = new Polygon();
        polygon.deserialize(deserializerBuffer);

        assertEquals(expected, polygon);
    }

    @Test
    void validateDeserialize_LITTLE_ENDIAN_with_SampleData() throws ValueDeserializationException {
        for (TestData<?> testData : TestData.SUCCESS_TEST_DATA) {
            DeserializerBuffer deserializerBuffer = new DeserializerBuffer(testData.getByteValueLittleEndian());
            switch (testData.getName()) {
                case TestData.BOOLEAN:
                    boolean valueBool = deserializerBuffer.readBool();
                    LOGGER.debug("Expected: {}, Actual: {} - Hex: {}", testData.getValue(), valueBool, testData.getByteValueLittleEndianHex());
                    assertEquals(testData.getValue(), valueBool);
                    break;
                case TestData.BYTE_ARRAY:
                    byte[] valueByteArray = deserializerBuffer.readByteArray(testData.getByteValueLittleEndian().length);
                    LOGGER.debug("Expected: {}, Actual: {} - Hex: {}", testData.getValue(), valueByteArray, testData.getByteValueLittleEndianHex());
                    assertArrayEquals((byte[]) testData.getValue(), valueByteArray);
                    break;
                case TestData.U_8:
                    byte valueU8 = deserializerBuffer.readU8();
                    LOGGER.debug("Expected: {}, Actual: {} - Hex: {}", testData.getValue(), valueU8, testData.getByteValueLittleEndianHex());
                    assertEquals(testData.getValue(), valueU8);
                    break;
                case TestData.U_16:
                    short valueU16 = deserializerBuffer.readU16();
                    LOGGER.debug("Expected: {}, Actual: {} - Hex: {}", testData.getValue(), valueU16, testData.getByteValueLittleEndianHex());
                    assertEquals(testData.getValue(), valueU16);
                    break;
                case TestData.U_32:
                    long valueU32 = deserializerBuffer.readU32();
                    LOGGER.debug("Expected: {}, Actual: {} - Hex: {}", testData.getValue(), valueU32, testData.getByteValueLittleEndianHex());
                    assertEquals(testData.getValue(), valueU32);
                    break;
                case TestData.F_32:
                    float valueF32 = deserializerBuffer.readF32();
                    LOGGER.debug("Expected: {}, Actual: {} - Hex: {}", testData.getValue(), valueF32, testData.getByteValueLittleEndianHex());
                    assertEquals(testData.getValue(), valueF32);
                    break;
                case TestData.F_64:
                    double valueF64 = deserializerBuffer.readF64();
                    LOGGER.debug("Expected: {}, Actual: {} - Hex: {}", testData.getValue(), valueF64, testData.getByteValueLittleEndianHex());
                    assertEquals(testData.getValue(), valueF64);
                    break;
                case TestData.U_64:
                    BigInteger valueU64 = deserializerBuffer.readU64();
                    LOGGER.debug("Expected: {}, Actual: {} - Hex: {}", testData.getValue(), valueU64, testData.getByteValueLittleEndianHex());
                    assertEquals(testData.getValue(), valueU64);
                    break;
                case TestData.I_32:
                    int valueI32 = deserializerBuffer.readI32();
                    LOGGER.debug("Expected: {}, Actual: {} - Hex: {}", testData.getValue(), valueI32, testData.getByteValueLittleEndianHex());
                    assertEquals(testData.getValue(), valueI32);
                    break;
                case TestData.I_64:
                    long valueI64 = deserializerBuffer.readI64();
                    LOGGER.debug("Expected: {}, Actual: {} - Hex: {}", testData.getValue(), valueI64, testData.getByteValueLittleEndianHex());
                    assertEquals(testData.getValue(), valueI64);
                    break;
                case TestData.U_128:
                    BigInteger valueU128 = deserializerBuffer.readU128();
                    LOGGER.debug("Expected: {}, Actual: {} - Hex: {}", testData.getValue(), valueU128, testData.getByteValueLittleEndianHex());
                    assertEquals(testData.getValue(), valueU128);
                    break;
                case TestData.U_256:
                    BigInteger valueU256 = deserializerBuffer.readU256();
                    LOGGER.debug("Expected: {}, Actual: {} - Hex: {}", testData.getValue(), valueU256, testData.getByteValueLittleEndianHex());
                    assertEquals(testData.getValue(), valueU256);
                    break;
                case TestData.U_512:
                    BigInteger valueU512 = deserializerBuffer.readU512();
                    LOGGER.debug("Expected: {}, Actual: {} - Hex: {}", testData.getValue(), valueU512, testData.getByteValueLittleEndianHex());
                    assertEquals(testData.getValue(), valueU512);
                    break;
                case TestData.STRING:
                    String valueString = deserializerBuffer.readString();
                    LOGGER.debug("Expected: {}, Actual: {} - Hex: {}", testData.getValue(), valueString, testData.getByteValueLittleEndianHex());
                    assertEquals(testData.getValue(), valueString);
                    break;
            }
        }
    }

    @Test
    void validateDeserialize_BIG_ENDIAN_with_SampleData() throws ValueDeserializationException {
        for (TestData<?> testData : TestData.SUCCESS_TEST_DATA) {
            DeserializerBuffer deserializerBuffer;
            switch (testData.getName()) {
                case TestData.BOOLEAN:
                    deserializerBuffer = new DeserializerBuffer(testData.getByteValueBigEndian());
                    boolean valueBool = deserializerBuffer.readBool();
                    LOGGER.debug("Expected: {}, Actual: {} - Hex: {}", testData.getValue(), valueBool, testData.getByteValueBigEndianHex());
                    assertEquals(testData.getValue(), valueBool);
                    break;
                case TestData.BYTE_ARRAY:
                    deserializerBuffer = new DeserializerBuffer(testData.getByteValueBigEndian(), ByteOrder.BIG_ENDIAN);
                    byte[] valueByteArray = deserializerBuffer.readByteArray(testData.getByteValueBigEndian().length);
                    LOGGER.debug("Expected: {}, Actual: {} - Hex: {}", testData.getValue(), valueByteArray, testData.getByteValueBigEndianHex());
                    assertArrayEquals((byte[]) testData.getValue(), valueByteArray);
                    break;
                case TestData.U_8:
                    deserializerBuffer = new DeserializerBuffer(testData.getByteValueBigEndian(), ByteOrder.BIG_ENDIAN);
                    byte valueU8 = deserializerBuffer.readU8();
                    LOGGER.debug("Expected: {}, Actual: {} - Hex: {}", testData.getValue(), valueU8, testData.getByteValueBigEndianHex());
                    assertEquals(testData.getValue(), valueU8);
                    break;
                case TestData.U_32:
                    deserializerBuffer = new DeserializerBuffer(testData.getByteValueBigEndian(), ByteOrder.BIG_ENDIAN);
                    long valueU32 = deserializerBuffer.readU32();
                    LOGGER.debug("Expected: {}, Actual: {} - Hex: {}", testData.getValue(), valueU32, testData.getByteValueBigEndianHex());
                    assertEquals(testData.getValue(), valueU32);
                    break;
                case TestData.F_32:
                    deserializerBuffer = new DeserializerBuffer(testData.getByteValueBigEndian(), ByteOrder.BIG_ENDIAN);
                    float valueF32 = deserializerBuffer.readF32();
                    LOGGER.debug("Expected: {}, Actual: {} - Hex: {}", testData.getValue(), valueF32, testData.getByteValueBigEndianHex());
                    assertEquals(testData.getValue(), valueF32);
                    break;
                case TestData.F_64:
                    deserializerBuffer = new DeserializerBuffer(testData.getByteValueBigEndian(), ByteOrder.BIG_ENDIAN);
                    double valueF64 = deserializerBuffer.readF64();
                    LOGGER.debug("Expected: {}, Actual: {} - Hex: {}", testData.getValue(), valueF64, testData.getByteValueBigEndianHex());
                    assertEquals(testData.getValue(), valueF64);
                    break;
                case TestData.U_64:
                    deserializerBuffer = new DeserializerBuffer(testData.getByteValueBigEndian(), ByteOrder.BIG_ENDIAN);
                    BigInteger valueU64 = deserializerBuffer.readU64();
                    LOGGER.debug("Expected: {}, Actual: {} - Hex: {}", testData.getValue(), valueU64, testData.getByteValueBigEndianHex());
                    assertEquals(testData.getValue(), valueU64);
                    break;
                case TestData.I_32:
                    deserializerBuffer = new DeserializerBuffer(testData.getByteValueBigEndian(), ByteOrder.BIG_ENDIAN);
                    int valueI32 = deserializerBuffer.readI32();
                    LOGGER.debug("Expected: {}, Actual: {} - Hex: {}", testData.getValue(), valueI32, testData.getByteValueBigEndianHex());
                    assertEquals(testData.getValue(), valueI32);
                    break;
                case TestData.I_64:
                    deserializerBuffer = new DeserializerBuffer(testData.getByteValueBigEndian(), ByteOrder.BIG_ENDIAN);
                    long valueI64 = deserializerBuffer.readI64();
                    LOGGER.debug("Expected: {}, Actual: {} - Hex: {}", testData.getValue(), valueI64, testData.getByteValueBigEndianHex());
                    assertEquals(testData.getValue(), valueI64);
                    break;
                case TestData.U_128:
                    deserializerBuffer = new DeserializerBuffer(testData.getByteValueBigEndian(), ByteOrder.BIG_ENDIAN);
                    BigInteger valueU128 = deserializerBuffer.readU128();
                    LOGGER.debug("Expected: {}, Actual: {} - Hex: {}", testData.getValue(), valueU128, testData.getByteValueBigEndianHex());
                    assertEquals(testData.getValue(), valueU128);
                    break;
                case TestData.U_256:
                    deserializerBuffer = new DeserializerBuffer(testData.getByteValueBigEndian(), ByteOrder.BIG_ENDIAN);
                    BigInteger valueU256 = deserializerBuffer.readU256();
                    LOGGER.debug("Expected: {}, Actual: {} - Hex: {}", testData.getValue(), valueU256, testData.getByteValueBigEndianHex());
                    assertEquals(testData.getValue(), valueU256);
                    break;
                case TestData.U_512:
                    deserializerBuffer = new DeserializerBuffer(testData.getByteValueBigEndian(), ByteOrder.BIG_ENDIAN);
                    BigInteger valueU512 = deserializerBuffer.readU512();
                    LOGGER.debug("Expected: {}, Actual: {} - Hex: {}", testData.getValue(), valueU512, testData.getByteValueBigEndianHex());
                    assertEquals(testData.getValue(), valueU512);
                    break;
                case TestData.STRING:
                    deserializerBuffer = new DeserializerBuffer(testData.getByteValueBigEndian(), ByteOrder.BIG_ENDIAN);
                    String valueString = deserializerBuffer.readString();
                    LOGGER.debug("Expected: {}, Actual: {} - Hex: {}", testData.getValue(), valueString, testData.getByteValueLittleEndianHex());
                    assertEquals(testData.getValue(), valueString);
                    break;
            }
        }
    }

    @Test
    void dataWithWrongInputLength_should_throw_BufferUnderflowException() {
        DeserializerBuffer deserializerBuffer1 = new DeserializerBuffer("");

        assertThrows(ValueDeserializationException.class, deserializerBuffer1::readBool);
        assertThrows(ValueDeserializationException.class, deserializerBuffer1::readU32);
        assertThrows(ValueDeserializationException.class, deserializerBuffer1::readU64);
        assertThrows(ValueDeserializationException.class, deserializerBuffer1::readU128);
        assertThrows(ValueDeserializationException.class, deserializerBuffer1::readU256);
        assertThrows(ValueDeserializationException.class, deserializerBuffer1::readU512);
        assertThrows(ValueDeserializationException.class, deserializerBuffer1::readString);
        assertThrows(ValueDeserializationException.class, () -> deserializerBuffer1.readByteArray(32));

        DeserializerBuffer deserializerBuffer2 = new DeserializerBuffer("01");

        assertThrows(ValueDeserializationException.class, deserializerBuffer2::readU512);

        DeserializerBuffer deserializerBuffer3 = new DeserializerBuffer("01");

        assertThrows(ValueDeserializationException.class, deserializerBuffer3::readString);
    }
}
