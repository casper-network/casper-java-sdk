package com.casper.sdk.model.clvalue.serde;

import com.casper.sdk.model.clvalue.*;
import com.casper.sdk.model.clvalue.cltype.AbstractCLType;
import dev.oak3.sbs4j.DeserializerBuffer;
import dev.oak3.sbs4j.SerializerBuffer;
import dev.oak3.sbs4j.exception.ValueDeserializationException;
import dev.oak3.sbs4j.exception.ValueSerializationException;
import lombok.Getter;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link SerializerBuffer} and {@link DeserializerBuffer}
 *
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
public class SerializerDeserializerTests {

    private static final Logger LOGGER = LoggerFactory.getLogger(SerializerDeserializerTests.class);

    /**
     * A container for test data to test CLValue Encoder/Decoder
     */
    private static class TestData<T> {
        @Getter
        private final String name;
        @Getter
        private final T value;
        @Getter
        private final String hexEncodedValue;

        @Getter
        private final Class<?>[] supportTypes;

        TestData(String name, T value, String hexEncodedValue, Class<?>[] supportTypes) {
            this.name = name;
            this.value = value;
            this.hexEncodedValue = hexEncodedValue;
            this.supportTypes = supportTypes;
        }
    }

    private final List<TestData<?>> successTestDataList = Arrays.asList(
            new TestData<>(AbstractCLType.BOOL, true, "01", null),
            new TestData<>(AbstractCLType.U8, Byte.valueOf("7"), "07", null),
            new TestData<>(AbstractCLType.U32, 7L, "07000000", null),
            new TestData<>(AbstractCLType.I32, 7, "07000000", null),
            new TestData<>(AbstractCLType.I64, 7L, "0700000000000000", null),
            new TestData<>(AbstractCLType.U64, new BigInteger("1024", 10), "0004000000000000", null),
            new TestData<>(AbstractCLType.U64, new BigInteger("18446744073709551615", 10), "ffffffffffffffff",
                    null),
            new TestData<>(AbstractCLType.U512, new BigInteger("7", 10), "0107", null),
            new TestData<>(AbstractCLType.U512, new BigInteger("1024", 10), "020004", null),
            new TestData<>(AbstractCLType.U512, new BigInteger("123456789101112131415", 10),
                    "0957ff1ada959f4eb106", null),
            new TestData<>(AbstractCLType.U512, new BigInteger("2500010000", 10), "0410200395", null),
            new TestData<>(AbstractCLType.STRING, "the string", "0a00000074686520737472696e67", null),
            new TestData<>(AbstractCLType.STRING, "Hello, World!", "0d00000048656c6c6f2c20576f726c6421", null));

    private final List<TestData<?>> lastValidNumberTestDataList = Arrays.asList(
            new TestData<>(AbstractCLType.U64, SerializerBuffer.MAX_U64, null, null),
            new TestData<>(AbstractCLType.U128, SerializerBuffer.MAX_U128, null, null),
            new TestData<>(AbstractCLType.U256, SerializerBuffer.MAX_U256, null, null),
            new TestData<>(AbstractCLType.U512, SerializerBuffer.MAX_U512, null, null));

    private final List<TestData<?>> outOfBoundsTestDataList = Arrays.asList(
            new TestData<>(AbstractCLType.U64, SerializerBuffer.MAX_U64.add(SerializerBuffer.ONE), null, null),
            new TestData<>(AbstractCLType.U128, SerializerBuffer.MAX_U128.add(SerializerBuffer.ONE), null, null),
            new TestData<>(AbstractCLType.U256, SerializerBuffer.MAX_U256.add(SerializerBuffer.ONE), null, null),
            new TestData<>(AbstractCLType.U512, SerializerBuffer.MAX_U512.add(SerializerBuffer.ONE), null, null));

    @Test
    void validateDecode_with_SampleData() throws ValueDeserializationException {
        for (TestData<?> testData : successTestDataList) {
            DeserializerBuffer deser = new DeserializerBuffer(testData.getHexEncodedValue());
            switch (testData.getName()) {
                case AbstractCLType.BOOL:
                    CLValueBool clValueBool = new CLValueBool();
                    clValueBool.deserialize(deser);
                    LOGGER.debug("Expected: {}, Actual: {}", testData.getValue(), clValueBool.getValue());
                    assertEquals(testData.getValue(), clValueBool.getValue());
                    break;
                case AbstractCLType.U8:
                    CLValueU8 clValueU8 = new CLValueU8();
                    clValueU8.deserialize(deser);
                    LOGGER.debug("Expected: {}, Actual: {}", testData.getValue(), clValueU8.getValue());
                    assertEquals(testData.getValue(), clValueU8.getValue());
                    break;
                case AbstractCLType.U32:
                    CLValueU32 clValueU32 = new CLValueU32();
                    clValueU32.deserialize(deser);
                    LOGGER.debug("Expected: {}, Actual: {}", testData.getValue(), clValueU32.getValue());
                    assertEquals(testData.getValue(), clValueU32.getValue());
                    break;
                case AbstractCLType.U64:
                    CLValueU64 clValueU64 = new CLValueU64();
                    clValueU64.deserialize(deser);
                    LOGGER.debug("Expected: {}, Actual: {}", testData.getValue(), clValueU64.getValue());
                    assertEquals(testData.getValue(), clValueU64.getValue());
                    break;
                case AbstractCLType.I32:
                    CLValueI32 clValueI32 = new CLValueI32();
                    clValueI32.deserialize(deser);
                    LOGGER.debug("Expected: {}, Actual: {}", testData.getValue(), clValueI32.getValue());
                    assertEquals(testData.getValue(), clValueI32.getValue());
                    break;
                case AbstractCLType.I64:
                    CLValueI64 clValueI64 = new CLValueI64();
                    clValueI64.deserialize(deser);
                    LOGGER.debug("Expected: {}, Actual: {}", testData.getValue(), clValueI64.getValue());
                    assertEquals(testData.getValue(), clValueI64.getValue());
                    break;
                case AbstractCLType.U128:
                    CLValueU128 clValueU128 = new CLValueU128();
                    clValueU128.deserialize(deser);
                    LOGGER.debug("Expected: {}, Actual: {}", testData.getValue(), clValueU128.getValue());
                    assertEquals(testData.getValue(), clValueU128.getValue());
                    break;
                case AbstractCLType.U256:
                    CLValueU256 clValueU256 = new CLValueU256();
                    clValueU256.deserialize(deser);
                    LOGGER.debug("Expected: {}, Actual: {}", testData.getValue(), clValueU256.getValue());
                    assertEquals(testData.getValue(), clValueU256.getValue());
                    break;
                case AbstractCLType.U512:
                    CLValueU512 clValueU512 = new CLValueU512();
                    clValueU512.deserialize(deser);
                    LOGGER.debug("Expected: {}, Actual: {}", testData.getValue(), clValueU512.getValue());
                    assertEquals(testData.getValue(), clValueU512.getValue());
                    break;
                case AbstractCLType.STRING:
                    CLValueString clValueString = new CLValueString();
                    clValueString.deserialize(deser);
                    LOGGER.debug("Expected: {}, Actual: {}", testData.getValue(), clValueString.getValue());
                    assertEquals(testData.getValue(), clValueString.getValue());
                    break;
            }
        }
    }

    @Test
    void validateEncode_with_SampleData() throws ValueSerializationException {
        for (TestData<?> testData : successTestDataList) {
            SerializerBuffer ser = new SerializerBuffer();
            switch (testData.getName()) {
                case AbstractCLType.BOOL:
                    CLValueBool clValueBool = new CLValueBool((Boolean) testData.getValue());
                    clValueBool.serialize(ser);
                    assertEquals(testData.getHexEncodedValue(), clValueBool.getBytes());
                    break;
                case AbstractCLType.U8:
                    CLValueU8 clValueU8 = new CLValueU8((Byte) testData.getValue());
                    clValueU8.serialize(ser);
                    assertEquals(testData.getHexEncodedValue(), clValueU8.getBytes());
                    break;
                case AbstractCLType.U32:
                    CLValueU32 clValueU32 = new CLValueU32((Long) testData.getValue());
                    clValueU32.serialize(ser);
                    assertEquals(testData.getHexEncodedValue(), clValueU32.getBytes());
                    break;
                case AbstractCLType.U64:
                    CLValueU64 clValueU64 = new CLValueU64((BigInteger) testData.getValue());
                    clValueU64.serialize(ser);
                    assertEquals(testData.getHexEncodedValue(), clValueU64.getBytes());
                    break;
                case AbstractCLType.I32:
                    CLValueI32 clValueI32 = new CLValueI32((Integer) testData.getValue());
                    clValueI32.serialize(ser);
                    assertEquals(testData.getHexEncodedValue(), clValueI32.getBytes());
                    break;
                case AbstractCLType.I64:
                    CLValueI64 clValueI64 = new CLValueI64((Long) testData.getValue());
                    clValueI64.serialize(ser);
                    assertEquals(testData.getHexEncodedValue(), clValueI64.getBytes());
                    break;
                case AbstractCLType.U128:
                    CLValueU128 clValueU128 = new CLValueU128((BigInteger) testData.getValue());
                    clValueU128.serialize(ser);
                    assertEquals(testData.getHexEncodedValue(), clValueU128.getBytes());
                    break;
                case AbstractCLType.U256:
                    CLValueU256 clValueU256 = new CLValueU256((BigInteger) testData.getValue());
                    clValueU256.serialize(ser);
                    assertEquals(testData.getHexEncodedValue(), clValueU256.getBytes());
                    break;
                case AbstractCLType.U512:
                    CLValueU512 clValueU512 = new CLValueU512((BigInteger) testData.getValue());
                    clValueU512.serialize(ser);
                    assertEquals(testData.getHexEncodedValue(), clValueU512.getBytes());
                    break;
                case AbstractCLType.STRING:
                    CLValueString clValueString = new CLValueString((String) testData.getValue());
                    clValueString.serialize(ser);
                    assertEquals(testData.getHexEncodedValue(), clValueString.getBytes());
                    break;
            }
        }
    }

    @Test
    void numbersShouldBeInsideTheirTypeBounds() throws ValueSerializationException {
        for (TestData<?> testData : lastValidNumberTestDataList) {
            SerializerBuffer ser = new SerializerBuffer();
            switch (testData.getName()) {
                case AbstractCLType.U64:
                    LOGGER.debug("Testing last valid number (value: {}) for {}", testData.getValue(),
                            AbstractCLType.U64);
                    CLValueU64 clValueU64 = new CLValueU64((BigInteger) testData.getValue());
                    clValueU64.serialize(ser);
                    assertNotNull(clValueU64.getBytes());
                    break;
                case AbstractCLType.U128:
                    LOGGER.debug("Testing last valid number (value: {}) for {}", testData.getValue(),
                            AbstractCLType.U128);
                    CLValueU128 clValueU128 = new CLValueU128((BigInteger) testData.getValue());
                    clValueU128.serialize(ser);
                    assertNotNull(clValueU128.getBytes());
                    break;
                case AbstractCLType.U256:
                    LOGGER.debug("Testing last valid number (value: {}) for {}", testData.getValue(),
                            AbstractCLType.U256);
                    CLValueU256 clValueU256 = new CLValueU256((BigInteger) testData.getValue());
                    clValueU256.serialize(ser);
                    assertNotNull(clValueU256.getBytes());
                    break;
                case AbstractCLType.U512:
                    LOGGER.debug("Testing last valid number (value: {}) for {}", testData.getValue(),
                            AbstractCLType.U512);
                    CLValueU512 clValueU512 = new CLValueU512((BigInteger) testData.getValue());
                    clValueU512.serialize(ser);
                    assertNotNull(clValueU512.getBytes());
                    break;
            }
        }
    }

    @Test
    void dataOutOfBounds_should_throw_ValueSerializationException() {
        for (TestData<?> testData : outOfBoundsTestDataList) {
            SerializerBuffer ser = new SerializerBuffer();
            switch (testData.getName()) {
                case AbstractCLType.U64:
                    assertThrows(ValueSerializationException.class, () -> {
                        CLValueU64 clValueU64 = new CLValueU64((BigInteger) testData.getValue());
                        clValueU64.serialize(ser);
                    });
                    break;
                case AbstractCLType.U128:
                    assertThrows(ValueSerializationException.class, () -> {
                        CLValueU128 clValueU128 = new CLValueU128((BigInteger) testData.getValue());
                        clValueU128.serialize(ser);
                    });
                    break;
                case AbstractCLType.U256:
                    assertThrows(ValueSerializationException.class, () -> {
                        CLValueU256 clValueU256 = new CLValueU256((BigInteger) testData.getValue());
                        clValueU256.serialize(ser);
                    });
                    break;
                case AbstractCLType.U512:
                    assertThrows(ValueSerializationException.class, () -> {
                        CLValueU512 clValueU512 = new CLValueU512((BigInteger) testData.getValue());
                        clValueU512.serialize(ser);
                    });
                    break;
            }
        }
    }

    @Test
    void dataWithWrongInputLength_should_throw_IllegalArgumentException_or_ValueDeserializationException() {

        assertThrows(IllegalArgumentException.class, () -> new DeserializerBuffer("0"));

        final DeserializerBuffer deser2 = new DeserializerBuffer("01");
        assertThrows(ValueDeserializationException.class, () -> new CLValueU512().deserialize(deser2));

        final DeserializerBuffer deser3 = new DeserializerBuffer("01");
        assertThrows(ValueDeserializationException.class, () -> new CLValueString().deserialize(deser3));
    }
}
