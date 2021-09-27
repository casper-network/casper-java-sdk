package com.syntifi.casper.sdk;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.syntifi.casper.sdk.exception.CLValueEncodeException;
import com.syntifi.casper.sdk.exception.DynamicInstanceException;
import com.syntifi.casper.sdk.model.Result;
import com.syntifi.casper.sdk.model.key.Algorithm;
import com.syntifi.casper.sdk.model.key.PublicKey;
import com.syntifi.casper.sdk.model.storedvalue.StoredValueData;
import com.syntifi.casper.sdk.model.storedvalue.clvalue.AbstractCLValue;
import com.syntifi.casper.sdk.model.storedvalue.clvalue.CLTypeData;
import com.syntifi.casper.sdk.model.storedvalue.clvalue.CLValueBool;
import com.syntifi.casper.sdk.model.storedvalue.clvalue.CLValueI32;
import com.syntifi.casper.sdk.model.storedvalue.clvalue.CLValueList;
import com.syntifi.casper.sdk.model.storedvalue.clvalue.CLValueMap;
import com.syntifi.casper.sdk.model.storedvalue.clvalue.CLValueOption;
import com.syntifi.casper.sdk.model.storedvalue.clvalue.CLValuePublicKey;
import com.syntifi.casper.sdk.model.storedvalue.clvalue.CLValueResult;
import com.syntifi.casper.sdk.model.storedvalue.clvalue.CLValueString;
import com.syntifi.casper.sdk.model.storedvalue.clvalue.CLValueTuple1;
import com.syntifi.casper.sdk.model.storedvalue.clvalue.CLValueTuple2;
import com.syntifi.casper.sdk.model.storedvalue.clvalue.CLValueTuple3;
import com.syntifi.casper.sdk.model.storedvalue.clvalue.CLValueU512;
import com.syntifi.casper.sdk.model.storedvalue.clvalue.CLValueU8;
import com.syntifi.casper.sdk.model.storedvalue.clvalue.CLValueURef;
import com.syntifi.casper.sdk.model.storedvalue.clvalue.CLValueUnit;
import com.syntifi.casper.sdk.model.storedvalue.clvalue.encdec.CLValueEncoder;
import com.syntifi.casper.sdk.model.uref.URef;
import com.syntifi.casper.sdk.model.uref.URefAccessRight;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.javatuples.Pair;
import org.javatuples.Triplet;
import org.javatuples.Unit;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Unit tests for {@link StoredValueData}
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
public class CLValuesTests {

    private static final Logger LOGGER = LoggerFactory.getLogger(CLValuesTests.class);

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Test
    void test_instance_types() throws DynamicInstanceException {
        for (CLTypeData clTypeData : CLTypeData.values()) {
            // Warn if there are any missing implementation
            if (clTypeData.getClazz() == null) {
                LOGGER.warn("CLType {} does not have an implementation!", clTypeData);
                continue;
            }

            AbstractCLValue<?> clValue = CLTypeData.createCLValueFromCLTypeData(clTypeData);

            // Correct instance type
            assertEquals(clTypeData.getClazz(), clValue.getClass());

            // Check if the correct CLType is set
            assertEquals(clTypeData, clValue.getClType().getClTypeData());
        }
    }

    @Test
    void test_u8() throws IOException {
        String inputJson = getPrettyJson(loadJsonFromFile("stored-value-samples/stored-value-u8.json"));

        LOGGER.debug("Original JSON: {}", inputJson);

        StoredValueData sv = OBJECT_MAPPER.readValue(inputJson, StoredValueData.class);
        // Should be CLValueU8
        assertTrue(sv.getCasperStoredValue().getClValue() instanceof CLValueU8);
        assertEquals((byte) 1, sv.getCasperStoredValue().getClValue().getValue());

        String reserializedJson = getPrettyJson(sv);

        LOGGER.debug("Serialized JSON: {}", reserializedJson);

        assertEquals(inputJson, reserializedJson);
    }

    @Test
    void test_string() throws IOException {
        String inputJson = getPrettyJson(loadJsonFromFile("stored-value-samples/stored-value-string.json"));

        LOGGER.debug("Original JSON: {}", inputJson);

        StoredValueData sv = OBJECT_MAPPER.readValue(inputJson, StoredValueData.class);
        // Should be string
        assertTrue(sv.getCasperStoredValue().getClValue() instanceof CLValueString);
        assertEquals("the string", sv.getCasperStoredValue().getClValue().getValue());

        String reserializedJson = getPrettyJson(sv);

        LOGGER.debug("Serialized JSON: {}", reserializedJson);

        assertEquals(inputJson, reserializedJson);
    }

    @Test
    void test_tuple1_bool() throws IOException, CLValueEncodeException, DynamicInstanceException {
        String inputJson = getPrettyJson(loadJsonFromFile("stored-value-samples/stored-value-tuple1-bool.json"));

        LOGGER.debug("Original JSON: {}", inputJson);

        StoredValueData sv = OBJECT_MAPPER.readValue(inputJson, StoredValueData.class);
        // Should be CLValueTuple1
        assertTrue(sv.getCasperStoredValue().getClValue() instanceof CLValueTuple1);
        CLValueTuple1 expected = new CLValueTuple1(new Unit<>(new CLValueBool(true)));
        try (CLValueEncoder clve = new CLValueEncoder()) {
            expected.encode(clve);
        }
        assertEquals(expected, sv.getCasperStoredValue().getClValue());

        String reserializedJson = getPrettyJson(sv);

        LOGGER.debug("Serialized JSON: {}", reserializedJson);

        assertEquals(inputJson, reserializedJson);
    }

    @Test
    void test_tuple2_i32_string() throws IOException, CLValueEncodeException, DynamicInstanceException {
        String inputJson = getPrettyJson(loadJsonFromFile("stored-value-samples/stored-value-tuple2-i32-string.json"));

        LOGGER.debug("Original JSON: {}", inputJson);

        StoredValueData sv = OBJECT_MAPPER.readValue(inputJson, StoredValueData.class);
        // Should be CLValueTuple2
        assertTrue(sv.getCasperStoredValue().getClValue() instanceof CLValueTuple2);
        CLValueTuple2 expected = new CLValueTuple2(new Pair<>(new CLValueI32(1), new CLValueString("Hello, World!")));
        try (CLValueEncoder clve = new CLValueEncoder()) {
            expected.encode(clve);
        }
        assertEquals(sv.getCasperStoredValue().getClValue(), expected);

        String reserializedJson = getPrettyJson(sv);

        LOGGER.debug("Serialized JSON: {}", reserializedJson);

        assertEquals(inputJson, reserializedJson);
    }

    @Test
    void test_tuple3_u8_string_bool() throws IOException, CLValueEncodeException, DynamicInstanceException {
        String inputJson = getPrettyJson(
                loadJsonFromFile("stored-value-samples/stored-value-tuple3-u8-string-bool.json"));

        LOGGER.debug("Original JSON: {}", inputJson);

        StoredValueData sv = OBJECT_MAPPER.readValue(inputJson, StoredValueData.class);
        // Should be CLValueTuple3
        assertTrue(sv.getCasperStoredValue().getClValue() instanceof CLValueTuple3);
        CLValueTuple3 expected = new CLValueTuple3(
                new Triplet<>(new CLValueU8((byte) 1), new CLValueString("Hello, World!"), new CLValueBool(true)));
        try (CLValueEncoder clve = new CLValueEncoder()) {
            expected.encode(clve);
        }
        assertEquals(sv.getCasperStoredValue().getClValue(), expected);

        String reserializedJson = getPrettyJson(sv);

        LOGGER.debug("Serialized JSON: {}", reserializedJson);

        assertEquals(inputJson, reserializedJson);
    }

    @Test
    void test_tuple3_i32_string_bool() throws IOException, CLValueEncodeException, DynamicInstanceException {
        String inputJson = getPrettyJson(
                loadJsonFromFile("stored-value-samples/stored-value-tuple3-i32-string-bool.json"));

        LOGGER.debug("Original JSON: {}", inputJson);

        StoredValueData sv = OBJECT_MAPPER.readValue(inputJson, StoredValueData.class);
        // Should be CLValueTuple3
        assertTrue(sv.getCasperStoredValue().getClValue() instanceof CLValueTuple3);
        CLValueTuple3 expected = new CLValueTuple3(
                new Triplet<>(new CLValueI32(1), new CLValueString("Hello, World!"), new CLValueBool(true)));
        try (CLValueEncoder clve = new CLValueEncoder()) {
            expected.encode(clve);
        }
        assertEquals(sv.getCasperStoredValue().getClValue(), expected);

        String reserializedJson = getPrettyJson(sv);

        LOGGER.debug("Serialized JSON: {}", reserializedJson);

        assertEquals(inputJson, reserializedJson);
    }

    @Test
    void test_tuple3_tuple1_bool_string_bool() throws IOException, CLValueEncodeException, DynamicInstanceException {
        String inputJson = getPrettyJson(
                loadJsonFromFile("stored-value-samples/stored-value-tuple3-tuple1-bool-string-bool.json"));

        LOGGER.debug("Original JSON: {}", inputJson);

        StoredValueData sv = OBJECT_MAPPER.readValue(inputJson, StoredValueData.class);
        // Should be CLValueTuple3
        assertTrue(sv.getCasperStoredValue().getClValue() instanceof CLValueTuple3);
        CLValueTuple3 expected = new CLValueTuple3(new Triplet<CLValueTuple1, CLValueString, CLValueBool>(
                new CLValueTuple1(new Unit<CLValueBool>(new CLValueBool(true))), new CLValueString("Hello, World!"),
                new CLValueBool(true)));
        try (CLValueEncoder clve = new CLValueEncoder()) {
            expected.encode(clve);
        }
        assertEquals(sv.getCasperStoredValue().getClValue(), expected);

        String reserializedJson = getPrettyJson(sv);

        LOGGER.debug("Serialized JSON: {}", reserializedJson);

        assertEquals(inputJson, reserializedJson);
    }

    @Test
    void test_tuple3_tuple1_u512_string_bool() throws IOException, CLValueEncodeException, DynamicInstanceException {
        String inputJson = getPrettyJson(
                loadJsonFromFile("stored-value-samples/stored-value-tuple3-tuple1-u512-string-bool.json"));

        LOGGER.debug("Original JSON: {}", inputJson);

        StoredValueData sv = OBJECT_MAPPER.readValue(inputJson, StoredValueData.class);
        // Should be CLValueTuple3
        assertTrue(sv.getCasperStoredValue().getClValue() instanceof CLValueTuple3);
        CLValueTuple3 expected = new CLValueTuple3(new Triplet<CLValueTuple1, CLValueString, CLValueBool>(
                new CLValueTuple1(new Unit<CLValueU512>(new CLValueU512(new BigInteger("123456789101112131415", 10)))),
                new CLValueString("Hello, World!"), new CLValueBool(true)));
        try (CLValueEncoder clve = new CLValueEncoder()) {
            expected.encode(clve);
        }
        assertEquals(sv.getCasperStoredValue().getClValue(), expected);

        String reserializedJson = getPrettyJson(sv);

        LOGGER.debug("Serialized JSON: {}", reserializedJson);

        assertEquals(inputJson, reserializedJson);
    }

    @Test
    void test_list_i32() throws IOException, CLValueEncodeException, DynamicInstanceException {
        String inputJson = getPrettyJson(loadJsonFromFile("stored-value-samples/stored-value-list-i32.json"));

        LOGGER.debug("Original JSON: {}", inputJson);

        StoredValueData sv = OBJECT_MAPPER.readValue(inputJson, StoredValueData.class);
        // Should be CLValueList
        assertTrue(sv.getCasperStoredValue().getClValue() instanceof CLValueList);
        CLValueList expected = new CLValueList(Arrays.asList(new CLValueI32(1), new CLValueI32(2), new CLValueI32(3)));
        try (CLValueEncoder clve = new CLValueEncoder()) {
            expected.encode(clve);
        }
        assertEquals(expected, sv.getCasperStoredValue().getClValue());

        String reserializedJson = getPrettyJson(sv);

        LOGGER.debug("Serialized JSON: {}", reserializedJson);

        assertEquals(inputJson, reserializedJson);
    }

    @Test
    void test_map_string_i32() throws IOException, CLValueEncodeException, DynamicInstanceException {
        String inputJson = getPrettyJson(loadJsonFromFile("stored-value-samples/stored-value-map-string-i32.json"));

        LOGGER.debug("Original JSON: {}", inputJson);

        StoredValueData sv = OBJECT_MAPPER.readValue(inputJson, StoredValueData.class);
        // Should be CLValueMap
        assertTrue(sv.getCasperStoredValue().getClValue() instanceof CLValueMap);
        Map<CLValueString, CLValueI32> map = new HashMap<>();
        map.put(new CLValueString("ABC"), new CLValueI32(10));
        CLValueMap expected = new CLValueMap(map);
        try (CLValueEncoder clve = new CLValueEncoder()) {
            expected.encode(clve);
        }
        assertEquals(expected, sv.getCasperStoredValue().getClValue());

        String reserializedJson = getPrettyJson(sv);

        LOGGER.debug("Serialized JSON: {}", reserializedJson);

        assertEquals(inputJson, reserializedJson);
    }

    @Test
    void test_result_i32_string() throws IOException, CLValueEncodeException, DynamicInstanceException {
        String inputJson = getPrettyJson(loadJsonFromFile("stored-value-samples/stored-value-result-i32-string.json"));

        LOGGER.debug("Original JSON: {}", inputJson);

        StoredValueData sv = OBJECT_MAPPER.readValue(inputJson, StoredValueData.class);
        // Should be CLValueMap
        assertTrue(sv.getCasperStoredValue().getClValue() instanceof CLValueResult);
        Result result = new Result();
        result.setOk(new CLValueI32(10));
        result.setErr(new CLValueString("Uh oh"));
        CLValueResult expected = new CLValueResult(result);
        try (CLValueEncoder clve = new CLValueEncoder()) {
            expected.encode(clve);
        }
        assertEquals(expected, sv.getCasperStoredValue().getClValue());

        String reserializedJson = getPrettyJson(sv);

        LOGGER.debug("Serialized JSON: {}", reserializedJson);

        assertEquals(inputJson, reserializedJson);
    }

    @Test
    void test_option_empty() throws IOException, CLValueEncodeException, DynamicInstanceException {
        String inputJson = getPrettyJson(loadJsonFromFile("stored-value-samples/stored-value-option-empty.json"));

        LOGGER.debug("Original JSON: {}", inputJson);

        StoredValueData sv = OBJECT_MAPPER.readValue(inputJson, StoredValueData.class);
        // Should be CLValueOption
        assertTrue(sv.getCasperStoredValue().getClValue() instanceof CLValueOption);
        CLValueOption expected = new CLValueOption(Optional.ofNullable(null));
        try (CLValueEncoder clve = new CLValueEncoder()) {
            expected.encode(clve);
        }
        assertEquals(expected, sv.getCasperStoredValue().getClValue());

        String reserializedJson = getPrettyJson(sv);

        LOGGER.debug("Serialized JSON: {}", reserializedJson);

        assertEquals(inputJson, reserializedJson);
    }

    @Test
    void test_option_bool() throws IOException, CLValueEncodeException, DynamicInstanceException {
        String inputJson = getPrettyJson(loadJsonFromFile("stored-value-samples/stored-value-option-bool.json"));

        LOGGER.debug("Original JSON: {}", inputJson);

        StoredValueData sv = OBJECT_MAPPER.readValue(inputJson, StoredValueData.class);
        // Should be CLValueOption
        assertTrue(sv.getCasperStoredValue().getClValue() instanceof CLValueOption);
        CLValueOption expected = new CLValueOption(Optional.of(new CLValueBool(true)));
        try (CLValueEncoder clve = new CLValueEncoder()) {
            expected.encode(clve);
        }
        assertEquals(expected, sv.getCasperStoredValue().getClValue());

        String reserializedJson = getPrettyJson(sv);

        LOGGER.debug("Serialized JSON: {}", reserializedJson);

        assertEquals(inputJson, reserializedJson);
    }

    @Test
    void test_option_i32() throws IOException, CLValueEncodeException, DynamicInstanceException {
        String inputJson = getPrettyJson(loadJsonFromFile("stored-value-samples/stored-value-option-i32.json"));

        LOGGER.debug("Original JSON: {}", inputJson);

        StoredValueData sv = OBJECT_MAPPER.readValue(inputJson, StoredValueData.class);
        // Should be CLValueOption
        assertTrue(sv.getCasperStoredValue().getClValue() instanceof CLValueOption);
        CLValueOption expected = new CLValueOption(Optional.of(new CLValueI32(10)));
        try (CLValueEncoder clve = new CLValueEncoder()) {
            expected.encode(clve);
        }
        assertEquals(expected, sv.getCasperStoredValue().getClValue());

        String reserializedJson = getPrettyJson(sv);

        LOGGER.debug("Serialized JSON: {}", reserializedJson);

        assertEquals(inputJson, reserializedJson);
    }

    @Test
    void test_unit() throws IOException, CLValueEncodeException, DynamicInstanceException {
        String inputJson = getPrettyJson(loadJsonFromFile("stored-value-samples/stored-value-unit.json"));

        LOGGER.debug("Original JSON: {}", inputJson);

        StoredValueData sv = OBJECT_MAPPER.readValue(inputJson, StoredValueData.class);
        // Should be CLValueOption
        assertTrue(sv.getCasperStoredValue().getClValue() instanceof CLValueUnit);
        CLValueUnit expected = new CLValueUnit();
        try (CLValueEncoder clve = new CLValueEncoder()) {
            expected.encode(clve);
        }
        assertEquals(expected, sv.getCasperStoredValue().getClValue());

        String reserializedJson = getPrettyJson(sv);

        LOGGER.debug("Serialized JSON: {}", reserializedJson);

        assertEquals(inputJson, reserializedJson);
    }

    @Test
    void test_uref_clvalue_mapping() throws IOException, DecoderException {
        String inputJson = getPrettyJson(loadJsonFromFile("stored-value-samples/stored-value-uref.json"));

        LOGGER.debug("Original JSON: {}", inputJson);

        StoredValueData sv = OBJECT_MAPPER.readValue(inputJson, StoredValueData.class);
        // Should be CLValuURef
        assertTrue(sv.getCasperStoredValue().getClValue() instanceof CLValueURef);
        assertEquals(new URef(
                Hex.decodeHex("2a2a2a2a2a2a2a2a2a2a2a2a2a2a2a2a2a2a2a2a2a2a2a2a2a2a2a2a2a2a2a2a".toCharArray()),
                URefAccessRight.READ_ADD_WRITE), sv.getCasperStoredValue().getClValue().getValue());

        String reserializedJson = getPrettyJson(sv);

        LOGGER.debug("Serialized JSON: {}", reserializedJson);

        assertEquals(inputJson, reserializedJson);
    }

    @Test
    void test_publicKey_clvalue_mapping() throws IOException, DecoderException {
        String inputJson = getPrettyJson(loadJsonFromFile("stored-value-samples/stored-value-publickey.json"));

        LOGGER.debug("Original JSON: {}", inputJson);

        StoredValueData sv = OBJECT_MAPPER.readValue(inputJson, StoredValueData.class);
        // Should be CLValuURef
        assertTrue(sv.getCasperStoredValue().getClValue() instanceof CLValuePublicKey);
        PublicKey expected = new PublicKey();
        expected.setAlgorithm(Algorithm.ED25519);
        expected.setKey(Hex.decodeHex("2a2a2a2a2a2a2a2a2a2a2a2a2a2a2a2a2a2a2a2a2a2a2a2a2a2a2a2a2a2a2a2a".toCharArray()));
        assertEquals(expected, sv.getCasperStoredValue().getClValue().getValue());

        String reserializedJson = getPrettyJson(sv);

        LOGGER.debug("Serialized JSON: {}", reserializedJson);

        assertEquals(inputJson, reserializedJson);
    }

    /**
     * Loads test json from resources
     * 
     * @param filename
     * @return
     * @throws IOException
     */
    String loadJsonFromFile(String filename) throws IOException {
        String fileJson;
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(filename)) {
            fileJson = new String(is.readAllBytes());
        }
        return fileJson;
    }

    /**
     * Prettifies json for assertion consistency
     * 
     * @param json json string to prettify
     * @return prettified json
     * @throws JsonMappingException
     * @throws JsonProcessingException
     */
    String getPrettyJson(String json) throws JsonMappingException, JsonProcessingException {
        Object jsonObject = OBJECT_MAPPER.readValue(json, Object.class);
        return getPrettyJson(jsonObject);
    }

    /**
     * Prettifies json for assertion consistency
     * 
     * @param jsonObject object to serialize and prettify
     * @return prettified json
     * @throws JsonMappingException
     * @throws JsonProcessingException
     */
    String getPrettyJson(Object jsonObject) throws JsonMappingException, JsonProcessingException {
        return OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObject);
    }
}