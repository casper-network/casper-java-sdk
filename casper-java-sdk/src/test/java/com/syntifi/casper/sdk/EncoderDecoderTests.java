package com.syntifi.casper.sdk;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

import com.syntifi.casper.sdk.exception.CLValueDecodeException;
import com.syntifi.casper.sdk.exception.CLValueEncodeException;
import com.syntifi.casper.sdk.exception.NoSuchCLTypeException;
import com.syntifi.casper.sdk.model.storedvalue.clvalue.CLType;
import com.syntifi.casper.sdk.model.storedvalue.clvalue.CLValueBool;
import com.syntifi.casper.sdk.model.storedvalue.clvalue.CLValueByteArray;
import com.syntifi.casper.sdk.model.storedvalue.clvalue.CLValueI32;
import com.syntifi.casper.sdk.model.storedvalue.clvalue.CLValueI64;
import com.syntifi.casper.sdk.model.storedvalue.clvalue.CLValueString;
import com.syntifi.casper.sdk.model.storedvalue.clvalue.CLValueU128;
import com.syntifi.casper.sdk.model.storedvalue.clvalue.CLValueU256;
import com.syntifi.casper.sdk.model.storedvalue.clvalue.CLValueU32;
import com.syntifi.casper.sdk.model.storedvalue.clvalue.CLValueU512;
import com.syntifi.casper.sdk.model.storedvalue.clvalue.CLValueU64;
import com.syntifi.casper.sdk.model.storedvalue.clvalue.CLValueU8;
import com.syntifi.casper.sdk.model.storedvalue.clvalue.encdec.CLValueDecoder;
import com.syntifi.casper.sdk.model.storedvalue.clvalue.encdec.CLValueEncoder;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.Getter;

/**
 * Unit tests for {@link CLValueEncoder} and {@link CLValueDecoder}
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
public class EncoderDecoderTests {

    private static final Logger LOGGER = LoggerFactory.getLogger(EncoderDecoderTests.class);

    /**
     * A container for test data to test CLValue Encoder/Decoder
     */
    private class TestData<T> {
        @Getter
        private String name;
        @Getter
        private T value;
        @Getter
        private String hexEncodedValue;

        @Getter
        private Class<?>[] supportTypes;

        TestData(String name, T value, String hexEncodedValue, Class<?>[] supportTypes) {
            this.name = name;
            this.value = value;
            this.hexEncodedValue = hexEncodedValue;
            this.supportTypes = supportTypes;
        }
    }

    private List<TestData<? extends Object>> successTestDataList = List.of(
            new TestData<Boolean>(CLType.BOOL, true, "01", null),
            new TestData<Byte>(CLType.U8, Byte.valueOf("7"), "07", null),
            new TestData<Long>(CLType.U32, Long.valueOf(7), "07000000", null),
            new TestData<Integer>(CLType.I32, Integer.valueOf(7), "07000000", null),
            new TestData<Long>(CLType.I64, Long.valueOf(7), "0700000000000000", null),
            new TestData<BigInteger>(CLType.U64, new BigInteger("1024", 10), "0004000000000000", null),
            new TestData<BigInteger>(CLType.U64, new BigInteger("18446744073709551615", 10), "ffffffffffffffff", null),
            new TestData<BigInteger>(CLType.U512, new BigInteger("7", 10), "0107", null),
            new TestData<BigInteger>(CLType.U512, new BigInteger("1024", 10), "020004", null),
            new TestData<BigInteger>(CLType.U512, new BigInteger("123456789101112131415", 10), "0957ff1ada959f4eb106",
                    null),
            new TestData<String>(CLType.STRING, "the string", "0a00000074686520737472696e67", null),
            new TestData<String>(CLType.STRING, "Hello, World!", "0d00000048656c6c6f2c20576f726c6421", null),
            new TestData<Optional<?>>(CLType.OPTION, Optional.ofNullable(null), "00", new Class<?>[] {}),
            new TestData<Optional<Boolean>>(CLType.OPTION, Optional.of(true), "0101", new Class<?>[] { Boolean.class }),
            new TestData<Optional<Integer>>(CLType.OPTION, Optional.of(10), "010a000000",
                    new Class<?>[] { Integer.class }));

    private List<TestData<? extends Object>> lastValidNumberTestDataList = List.of(
            new TestData<BigInteger>(CLType.U64, CLValueEncoder.MAX_U64, null, null),
            new TestData<BigInteger>(CLType.U128, CLValueEncoder.MAX_U128, null, null),
            new TestData<BigInteger>(CLType.U256, CLValueEncoder.MAX_U256, null, null),
            new TestData<BigInteger>(CLType.U512, CLValueEncoder.MAX_U512, null, null));

    private List<TestData<? extends Object>> outOfBoundsTestDataList = List.of(
            new TestData<BigInteger>(CLType.U64, CLValueEncoder.MAX_U64.add(CLValueEncoder.ONE), null, null),
            new TestData<BigInteger>(CLType.U128, CLValueEncoder.MAX_U128.add(CLValueEncoder.ONE), null, null),
            new TestData<BigInteger>(CLType.U256, CLValueEncoder.MAX_U256.add(CLValueEncoder.ONE), null, null),
            new TestData<BigInteger>(CLType.U512, CLValueEncoder.MAX_U512.add(CLValueEncoder.ONE), null, null));

    @Test
    void should_be_able_to_decode_sample_data() throws IOException, CLValueDecodeException {
        for (TestData<?> testData : successTestDataList) {
            try (CLValueDecoder decoder = new CLValueDecoder(testData.getHexEncodedValue())) {
                switch (testData.getName()) {
                    case CLType.BOOL:
                        CLValueBool clValueBool = new CLValueBool();
                        decoder.readBool(clValueBool);
                        LOGGER.debug("Expected: {}, Actual: {}", testData.getValue(), clValueBool.getValue());
                        assertEquals(testData.getValue(), clValueBool.getValue());
                        break;
                    case CLType.U8:
                        CLValueU8 clValueU8 = new CLValueU8();
                        decoder.readU8(clValueU8);
                        LOGGER.debug("Expected: {}, Actual: {}", testData.getValue(), clValueU8.getValue());
                        assertEquals(testData.getValue(), clValueU8.getValue());
                        break;
                    case CLType.U32:
                        CLValueU32 clValueU32 = new CLValueU32();
                        decoder.readU32(clValueU32);
                        LOGGER.debug("Expected: {}, Actual: {}", testData.getValue(), clValueU32.getValue());
                        assertEquals(testData.getValue(), clValueU32.getValue());
                        break;
                    case CLType.U64:
                        CLValueU64 clValueU64 = new CLValueU64();
                        decoder.readU64(clValueU64);
                        LOGGER.debug("Expected: {}, Actual: {}", testData.getValue(), clValueU64.getValue());
                        assertEquals(testData.getValue(), clValueU64.getValue());
                        break;
                    case CLType.I32:
                        CLValueI32 clValueI32 = new CLValueI32();
                        decoder.readI32(clValueI32);
                        LOGGER.debug("Expected: {}, Actual: {}", testData.getValue(), clValueI32.getValue());
                        assertEquals(testData.getValue(), clValueI32.getValue());
                        break;
                    case CLType.I64:
                        CLValueI64 clValueI64 = new CLValueI64();
                        decoder.readI64(clValueI64);
                        LOGGER.debug("Expected: {}, Actual: {}", testData.getValue(), clValueI64.getValue());
                        assertEquals(testData.getValue(), clValueI64.getValue());
                        break;
                    case CLType.U128:
                        CLValueU128 clValueU128 = new CLValueU128();
                        decoder.readU128(clValueU128);
                        LOGGER.debug("Expected: {}, Actual: {}", testData.getValue(), clValueU128.getValue());
                        assertEquals(testData.getValue(), clValueU128.getValue());
                        break;
                    case CLType.U256:
                        CLValueU256 clValueU256 = new CLValueU256();
                        decoder.readU256(clValueU256);
                        LOGGER.debug("Expected: {}, Actual: {}", testData.getValue(), clValueU256.getValue());
                        assertEquals(testData.getValue(), clValueU256.getValue());
                        break;
                    case CLType.U512:
                        CLValueU512 clValueU512 = new CLValueU512();
                        decoder.readU512(clValueU512);
                        LOGGER.debug("Expected: {}, Actual: {}", testData.getValue(), clValueU512.getValue());
                        assertEquals(testData.getValue(), clValueU512.getValue());
                        break;
                    case CLType.STRING:
                        CLValueString clValueString = new CLValueString();
                        decoder.readString(clValueString);
                        LOGGER.debug("Expected: {}, Actual: {}", testData.getValue(), clValueString.getValue());
                        assertEquals(testData.getValue(), clValueString.getValue());
                        break;
                    // case CLType.OPTION:
                    // boolean isPresent = decoder.readBool();
                    // Optional<?> byteOptional;
                    // // FIXME: Find a better testing strategy
                    // if (isPresent) {
                    // if (testData.supportTypes.length > 0) {
                    // if (testData.supportTypes[0] == Boolean.class) {
                    // byteOptional = Optional.of(decoder.readBool());
                    // } else if (testData.supportTypes[0] == Integer.class) {
                    // byteOptional = Optional.of(decoder.readI32());
                    // } else {
                    // byteOptional = Optional.ofNullable(null);
                    // }
                    // } else {
                    // byteOptional = Optional.ofNullable(null);
                    // }
                    // } else {
                    // byteOptional = Optional.ofNullable(null);
                    // }
                    // LOGGER.debug("Expected: {}, Actual: {}", testData.getValue(), byteOptional);
                    // assertEquals(testData.getValue(), byteOptional);
                    // break;
                }
            }
        }
    }

    @Test
    void should_be_able_to_encode_sample_data() throws IOException, CLValueEncodeException, NoSuchCLTypeException {
        for (TestData<?> testData : successTestDataList) {
            try (CLValueEncoder encoder = new CLValueEncoder()) {
                switch (testData.getName()) {
                    case CLType.BOOL:
                        CLValueBool clValueBool = new CLValueBool((Boolean) testData.getValue());
                        encoder.writeBool(clValueBool);
                        assertEquals(testData.getHexEncodedValue(), clValueBool.getBytes());
                        break;
                    case CLType.U8:
                        CLValueU8 clValueU8 = new CLValueU8((Byte) testData.getValue());
                        encoder.writeU8(clValueU8);
                        assertEquals(testData.getHexEncodedValue(), clValueU8.getBytes());
                        break;
                    case CLType.U32:
                        CLValueU32 clValueU32 = new CLValueU32((Long) testData.getValue());
                        encoder.writeU32(clValueU32);
                        assertEquals(testData.getHexEncodedValue(), clValueU32.getBytes());
                        break;
                    case CLType.U64:
                        CLValueU64 clValueU64 = new CLValueU64((BigInteger) testData.getValue());
                        encoder.writeU64(clValueU64);
                        assertEquals(testData.getHexEncodedValue(), clValueU64.getBytes());
                        break;
                    case CLType.I32:
                        CLValueI32 clValueI32 = new CLValueI32((Integer) testData.getValue());
                        encoder.writeI32(clValueI32);
                        assertEquals(testData.getHexEncodedValue(), clValueI32.getBytes());
                        break;
                    case CLType.I64:
                        CLValueI64 clValueI64 = new CLValueI64((Long) testData.getValue());
                        encoder.writeI64(clValueI64);
                        assertEquals(testData.getHexEncodedValue(), clValueI64.getBytes());
                        break;
                    case CLType.U128:
                        CLValueU128 clValueU128 = new CLValueU128((BigInteger) testData.getValue());
                        encoder.writeU128(clValueU128);
                        assertEquals(testData.getHexEncodedValue(), clValueU128.getBytes());
                        break;
                    case CLType.U256:
                        CLValueU256 clValueU256 = new CLValueU256((BigInteger) testData.getValue());
                        encoder.writeU256(clValueU256);
                        assertEquals(testData.getHexEncodedValue(), clValueU256.getBytes());
                        break;
                    case CLType.U512:
                        CLValueU512 clValueU512 = new CLValueU512((BigInteger) testData.getValue());
                        encoder.writeU512(clValueU512);
                        assertEquals(testData.getHexEncodedValue(), clValueU512.getBytes());
                        break;
                    case CLType.STRING:
                        CLValueString clValueString = new CLValueString((String) testData.getValue());
                        encoder.writeString(clValueString);
                        assertEquals(testData.getHexEncodedValue(), clValueString.getBytes());
                        break;
                }
            }
        }
    }

    @Test
    void should_be_inside_bounds() throws IOException, CLValueEncodeException, NoSuchCLTypeException {
        for (TestData<?> testData : lastValidNumberTestDataList) {
            try (CLValueEncoder encoder = new CLValueEncoder()) {
                switch (testData.getName()) {
                    case CLType.U64:
                        CLValueU64 clValueU64 = new CLValueU64((BigInteger) testData.getValue());
                        encoder.writeU64(clValueU64);
                        assertNotNull(clValueU64.getBytes());
                        break;
                    case CLType.U128:
                        CLValueU128 clValueU128 = new CLValueU128((BigInteger) testData.getValue());
                        encoder.writeU128(clValueU128);
                        assertNotNull(clValueU128.getBytes());
                        break;
                    case CLType.U256:
                        CLValueU256 clValueU256 = new CLValueU256((BigInteger) testData.getValue());
                        encoder.writeU256(clValueU256);
                        assertNotNull(clValueU256.getBytes());
                        break;
                    case CLType.U512:
                        CLValueU512 clValueU512 = new CLValueU512((BigInteger) testData.getValue());
                        encoder.writeU512(clValueU512);
                        assertNotNull(clValueU512.getBytes());
                        break;
                }
            }
        }
    }

    @Test
    void should_throw_CLValueEncodeException_for_out_of_bounds()
            throws IOException, CLValueEncodeException, NoSuchCLTypeException {
        for (TestData<?> testData : outOfBoundsTestDataList) {
            try (CLValueEncoder encoder = new CLValueEncoder()) {
                switch (testData.getName()) {
                    case CLType.U64:
                        assertThrows(CLValueEncodeException.class, () -> {
                            CLValueU64 clValueU64 = new CLValueU64((BigInteger) testData.getValue());
                            encoder.writeU64(clValueU64);
                        });
                        break;
                    case CLType.U128:
                        assertThrows(CLValueEncodeException.class, () -> {
                            CLValueU128 clValueU128 = new CLValueU128((BigInteger) testData.getValue());
                            encoder.writeU128(clValueU128);
                        });
                        break;
                    case CLType.U256:
                        assertThrows(CLValueEncodeException.class, () -> {
                            CLValueU256 clValueU256 = new CLValueU256((BigInteger) testData.getValue());
                            encoder.writeU256(clValueU256);
                        });
                        break;
                    case CLType.U512:
                        assertThrows(CLValueEncodeException.class, () -> {
                            CLValueU512 clValueU512 = new CLValueU512((BigInteger) testData.getValue());
                            encoder.writeU512(clValueU512);
                        });
                        break;
                }
            }
        }

    }

    @Test
    void should_throw_CLValueDecodeException_for_wrong_input_length()
            throws IOException, CLValueDecodeException, NoSuchCLTypeException {
        try (CLValueDecoder decoder = new CLValueDecoder("")) {
            assertThrows(CLValueDecodeException.class, () -> decoder.readBool(new CLValueBool()));
            assertThrows(CLValueDecodeException.class, () -> decoder.readU32(new CLValueU32()));
            assertThrows(CLValueDecodeException.class, () -> decoder.readU64(new CLValueU64()));
            assertThrows(CLValueDecodeException.class, () -> decoder.readU128(new CLValueU128()));
            assertThrows(CLValueDecodeException.class, () -> decoder.readU256(new CLValueU256()));
            assertThrows(CLValueDecodeException.class, () -> decoder.readU512(new CLValueU512()));
            assertThrows(CLValueDecodeException.class, () -> decoder.readString(new CLValueString()));
            assertThrows(CLValueDecodeException.class, () -> decoder.readByteArray(new CLValueByteArray(), 32));
        }
        try (CLValueDecoder decoder = new CLValueDecoder("01")) {
            assertThrows(CLValueDecodeException.class, () -> decoder.readU512(new CLValueU512()));
        }
        try (CLValueDecoder decoder = new CLValueDecoder("01")) {
            assertThrows(CLValueDecodeException.class, () -> decoder.readString(new CLValueString()));
        }
    }
}
