package com.casper.sdk.model.clvalue;

import com.casper.sdk.exception.DynamicInstanceException;
import com.casper.sdk.exception.NoSuchTypeException;
import com.casper.sdk.model.clvalue.cltype.*;
import com.casper.sdk.model.clvalue.serde.Target;
import com.casper.sdk.model.deploy.NamedArg;
import com.syntifi.crypto.key.encdec.Hex;
import dev.oak3.sbs4j.DeserializerBuffer;
import dev.oak3.sbs4j.SerializerBuffer;
import org.javatuples.Pair;
import org.javatuples.Triplet;
import org.javatuples.Unit;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.*;

public class CLValueTests {

    @Test
    void allCLTypes_must_be_implemented() throws DynamicInstanceException, NoSuchTypeException {
        for (CLTypeData clTypeData : CLTypeData.values()) {
            // Ensure there are no types with missing implementation
            assertNotNull(clTypeData.getClazz());

            AbstractCLValue<?, ?> clValue = CLTypeData.createCLValueFromCLTypeData(clTypeData);

            // Correct instance type
            assertEquals(clTypeData.getClazz(), clValue.getClass());

            // Check if the correct CLType is set
            assertEquals(clTypeData, clValue.getClType().getClTypeData());
        }
    }

    @Test
    void getTypeBySerializationTag_should_return_correct_CLTypeData() throws NoSuchTypeException {
        for (CLTypeData typeData : CLTypeData.values()) {
            assertEquals(typeData, CLTypeData.getTypeBySerializationTag(typeData.getSerializationTag()));
        }
    }

    @Test
    void createCLTypeFromCLTypeData_should_return_correct_CLType() throws DynamicInstanceException {
        assertInstanceOf(CLTypeAny.class, CLTypeData.createCLTypeFromCLTypeData(CLTypeData.ANY));
    }

    @Test
    void createCLValueFromCLTypeName_should_return_correct_CLValue() throws DynamicInstanceException, NoSuchTypeException {
        assertInstanceOf(CLValueAny.class, CLTypeData.createCLValueFromCLTypeName(CLTypeData.ANY.getClTypeName()));
    }

    @Test
    void createCLValueFromCLTypeName_from_CLTypeData_should_throw_NoSuchTypeException() {
        assertThrows(NoSuchTypeException.class, () -> CLTypeData.createCLValueFromCLTypeName("NE"));
    }

    @Test
    void getClassByName_from_CLTypeData_should_throw_NoSuchTypeException() {
        assertThrows(NoSuchTypeException.class, () -> CLTypeData.getClassByName("NE"));
    }

    @Test
    void getCLTypeClassByName_from_CLTypeData_should_throw_NoSuchTypeException() {
        assertThrows(NoSuchTypeException.class, () -> CLTypeData.getCLTypeClassByName("NE"));
    }

    @Test
    void clValueAnyByteSerializationWithAndWithoutTypeInfo() throws Exception {

        final CLValueAny clValueAny = new CLValueAny(Hex.decode("d2029649"));

        SerializerBuffer ser = new SerializerBuffer();
        clValueAny.serialize(ser, Target.JSON);
        assertThat(ser.toByteArray(), is(Hex.decode("d2029649")));

        ser = new SerializerBuffer();
        clValueAny.serialize(ser, Target.BYTE);
        assertThat(ser.toByteArray(), is(Hex.decode("04000000d202964915")));
    }


    @Test
    void clValueTupleOneSerialization() throws Exception {

        final CLValueTuple1 clValueTuple1 = new CLValueTuple1(new Unit<>(new CLValueU32(1L)));

        SerializerBuffer ser = new SerializerBuffer();
        clValueTuple1.serialize(ser, Target.JSON);
        assertThat(ser.toByteArray(), is(Hex.decode("01000000")));

        ser = new SerializerBuffer();
        clValueTuple1.serialize(ser, Target.BYTE);

        byte[] expected = {4, 0, 0, 0, 1, 0, 0, 0, 18, 4};
        assertThat(ser.toByteArray(), is(expected));

        NamedArg<CLTypeTuple1> namedArg = new NamedArg<>("TUPLE_1", clValueTuple1);
        ser = new SerializerBuffer();
        namedArg.serialize(ser, Target.BYTE);
        expected = new byte[]{7, 0, 0, 0, 84, 85, 80, 76, 69, 95, 49, 4, 0, 0, 0, 1, 0, 0, 0, 18, 4};

        assertThat(ser.toByteArray(), is(expected));
    }

    @Test
    void getTypeByName_from_CLTypeData_should_throw_NoSuchTypeException() {
        assertThrows(NoSuchTypeException.class, () -> CLTypeData.getTypeByName("NE"));
    }


    @Test
    void mapDeserialization() throws Exception {

        final CLValueString key = new CLValueString("ONE");
        final CLValueU32 value = new CLValueU32(2L);
        final Map<CLValueString, CLValueU32> innerMap = new HashMap<>();
        innerMap.put(key, value);
        CLValueMap clValueMap = new CLValueMap(innerMap);

        SerializerBuffer ser = new SerializerBuffer();
        clValueMap.serialize(ser, Target.JSON);
        byte[] bytes = ser.toByteArray();

        byte[] expected = Hex.decode("01000000030000004f4e4502000000");
        assertThat(bytes, is(expected));

        ser = new SerializerBuffer();
        clValueMap.serialize(ser, Target.BYTE);
        bytes = ser.toByteArray();
        expected = Hex.decode("0f00000001000000030000004f4e4502000000110a04");
        assertThat(bytes, is(expected));

        final CLValueMap deserialized = new CLValueMap();
        CLTypeMap clTypeMap = new CLTypeMap();
        clTypeMap.setKeyValueTypes(new CLTypeMap.CLTypeMapEntryType(key.getClType(), value.getClType()));
        deserialized.setClType(clTypeMap);
        deserialized.deserialize(new DeserializerBuffer("01000000030000004f4e4502000000"));
        String hexBytes = deserialized.getBytes();
        assertThat(hexBytes, is("01000000030000004f4e4502000000"));

        CLValueString keyDeser = (CLValueString) deserialized.getValue().keySet().iterator().next();

        assertThat(keyDeser, is(key));
        assertThat(keyDeser.getBytes(), is(key.getBytes()));

        CLValueU32 valueDeser = (CLValueU32) deserialized.getValue().get(key);
        assertThat(valueDeser, is(value));
        assertThat(valueDeser.getBytes(), is(value.getBytes()));
    }

    @Test
    void nestedTuple1Serialization() throws Exception {
        final CLValueTuple1 innerTuple1 = new CLValueTuple1(new Unit<>(new CLValueU32(1L)));
        final CLValueTuple1 outerTuple1 = new CLValueTuple1(new Unit<>(innerTuple1));

        final SerializerBuffer ser = new SerializerBuffer();
        outerTuple1.serialize(ser, Target.BYTE);

        final byte[] expected = {4, 0, 0, 0, 1, 0, 0, 0, 18, 18, 4};

        assertThat(ser.toByteArray(), is(expected));

        final CLValueTuple1 deserialized = (CLValueTuple1) outerTuple1.deserialize(new DeserializerBuffer(expected), Target.BYTE);
        assertThat(deserialized.getBytes(), is(outerTuple1.getBytes()));
    }

    @Test
    void nestedTuple2Serialization() throws Exception {
        final CLValueTuple2 innerTuple1 = new CLValueTuple2(new Pair<>(new CLValueU32(1L), new CLValueU32(2L)));
        final CLValueTuple2 innerTuple2 = new CLValueTuple2(new Pair<>(new CLValueU32(3L), new CLValueU32(4L)));
        final CLValueTuple2 outerTuple = new CLValueTuple2(new Pair<>(innerTuple1, innerTuple2));

        final SerializerBuffer ser = new SerializerBuffer();
        outerTuple.serialize(ser, Target.BYTE);

        final byte[] expected = {16, 0, 0, 0, 1, 0, 0, 0, 2, 0, 0, 0, 3, 0, 0, 0, 4, 0, 0, 0, 19, 19, 4, 4, 19, 4, 4};

        assertThat(ser.toByteArray(), is(expected));
    }

    @Test
    void nestedTuple3Serialization() throws Exception {
        final CLValueTuple3 innerTuple1 = new CLValueTuple3(new Triplet<>(new CLValueU32(1L), new CLValueU32(2L), new CLValueU32(3L)));
        final CLValueTuple3 innerTuple2 = new CLValueTuple3(new Triplet<>(innerTuple1, new CLValueU32(4L), new CLValueU32(5L)));
        final CLValueTuple3 outerTuple = new CLValueTuple3(new Triplet<>(innerTuple2, new CLValueU32(6L), new CLValueU32(7L)));

        final SerializerBuffer ser = new SerializerBuffer();
        outerTuple.serialize(ser, Target.BYTE);

        final byte[] expected = {28, 0, 0, 0, 1, 0, 0, 0, 2, 0, 0, 0, 3, 0, 0, 0, 4, 0, 0, 0, 5, 0, 0, 0, 6, 0, 0, 0, 7, 0, 0, 0, 20, 20, 20, 4, 4, 4, 4, 4, 4, 4};

        assertThat(ser.toByteArray(), is(expected));
    }

    @Test
    void nestedMapSerialization() throws Exception {

        Map<CLValueString, CLValueU32> map = new LinkedHashMap<>();
        map.put(new CLValueString("ONE"), new CLValueU32(1L));
        final CLValueMap innerMap1 = new CLValueMap(map);

        map = new LinkedHashMap<>();
        map.put(new CLValueString("TWO"), new CLValueU32(2L));
        final CLValueMap innerMap2 = new CLValueMap(map);


        Map<CLValueString, CLValueMap> map2 = new LinkedHashMap<>();
        map2.put(new CLValueString("THREE"), innerMap1);
        map2.put(new CLValueString("FOUR"), innerMap2);

        final CLValueMap outerMap = new CLValueMap(map2);

        final SerializerBuffer ser = new SerializerBuffer();
        outerMap.serialize(ser, Target.BYTE);

        byte[] expected = {51, 0, 0, 0, 2, 0, 0, 0, 5, 0, 0, 0, 84, 72, 82, 69, 69, 1, 0, 0, 0, 3, 0, 0, 0, 79, 78, 69, 1, 0, 0, 0, 4, 0, 0, 0, 70, 79, 85, 82, 1, 0, 0, 0, 3, 0, 0, 0, 84, 87, 79, 2, 0, 0, 0, 17, 10, 17, 10, 4};

        assertThat(ser.toByteArray(), is(expected));
    }

    @Test
    void nestedMapDeserialization() throws Exception {

        byte[] bytes = {51, 0, 0, 0, 2, 0, 0, 0, 5, 0, 0, 0, 84, 72, 82, 69, 69, 1, 0, 0, 0, 3, 0, 0, 0, 79, 78, 69, 1, 0, 0, 0, 4, 0, 0, 0, 70, 79, 85, 82, 1, 0, 0, 0, 3, 0, 0, 0, 84, 87, 79, 2, 0, 0, 0, 17, 10, 17, 10, 4};

        final CLTypeMap innerType = new CLTypeMap();
        innerType.setKeyValueTypes(new CLTypeMap.CLTypeMapEntryType(new CLTypeString(), new CLTypeU32()));

        final CLTypeMap outerType = new CLTypeMap();
        outerType.setKeyValueTypes(new CLTypeMap.CLTypeMapEntryType(new CLTypeString(), innerType));

        final CLValueMap outerMap = new CLValueMap();
        outerMap.setClType(outerType);
        outerMap.deserialize(new DeserializerBuffer(bytes), Target.BYTE);
    }

    @SuppressWarnings("unchecked")
    @Test
    void nestedMapDeserialization2() throws Exception {

        //  a nested map is created  {1: {11: {111: "ONE_ONE_ONE"}, 12: {121: "ONE_TWO_ONE"}}, 2: {21: {211: "TWO_ONE_ONE"}, 22: {221: "TWO_TWO_ONE"}}}
        //noinspection rawtypes
        Map innerMap = new LinkedHashMap<>();
        innerMap.put(new CLValueU256(BigInteger.valueOf(111L)), new CLValueString("ONE_ONE_ONE"));
        final CLValueMap innerMap111 = new CLValueMap(innerMap);

        innerMap = new LinkedHashMap<>();
        innerMap.put(new CLValueU256(BigInteger.valueOf(121L)), new CLValueString("ONE_TWO_ONE"));
        final CLValueMap innerMap121 = new CLValueMap(innerMap);

        innerMap = new LinkedHashMap<>();
        innerMap.put(new CLValueU256(BigInteger.valueOf(11L)), innerMap111);
        innerMap.put(new CLValueU256(BigInteger.valueOf(12L)), innerMap121);
        final CLValueMap innerMap1 = new CLValueMap(innerMap);

        innerMap = new LinkedHashMap<>();
        innerMap.put(new CLValueU256(BigInteger.valueOf(211L)), new CLValueString("TWO_ONE_ONE"));
        final CLValueMap innerMap21 = new CLValueMap(innerMap);

        innerMap = new LinkedHashMap<>();
        innerMap.put(new CLValueU256(BigInteger.valueOf(221)), new CLValueString("TWO_TWO_ONE"));
        final CLValueMap innerMap22 = new CLValueMap(innerMap);

        innerMap = new LinkedHashMap<>();
        innerMap.put(new CLValueU256(BigInteger.valueOf(21L)), innerMap21);
        innerMap.put(new CLValueU256(BigInteger.valueOf(22L)), innerMap22);
        final CLValueMap innerMap2 = new CLValueMap(innerMap);

        innerMap = new LinkedHashMap<>();
        innerMap.put(new CLValueU256(BigInteger.valueOf(1L)), innerMap1);
        innerMap.put(new CLValueU256(BigInteger.valueOf(2L)), innerMap2);
        final CLValueMap outerMap = new CLValueMap(innerMap);

        final SerializerBuffer ser = new SerializerBuffer();
        outerMap.serialize(ser, Target.BYTE);

        byte[] actualBytes = ser.toByteArray();

        assertThat(actualBytes.length, is(greaterThan(0)));

        CLValueMap clValueMap = new CLValueMap();
        clValueMap = (CLValueMap) clValueMap.deserialize(new DeserializerBuffer(actualBytes), Target.BYTE);

        assertThat(clValueMap.getBytes(), is(outerMap.getBytes()));
    }

    @Test
    void nestedListSerialization() throws Exception {

        final byte[] expectedBytes = {24, 0, 0, 0, 2, 0, 0, 0, 3, 0, 0, 0, 1, 1, 1, 2, 1, 3, 3, 0, 0, 0, 1, 4, 1, 5, 1, 6, 14, 14, 7};

        List<CLValueU256> innerInernalList = new ArrayList<>();
        innerInernalList.add(new CLValueU256(BigInteger.valueOf(1L)));
        innerInernalList.add(new CLValueU256(BigInteger.valueOf(2L)));
        innerInernalList.add(new CLValueU256(BigInteger.valueOf(3L)));
        CLValueList innerList1 = new CLValueList(innerInernalList);

        innerInernalList = new ArrayList<>();
        innerInernalList.add(new CLValueU256(BigInteger.valueOf(4L)));
        innerInernalList.add(new CLValueU256(BigInteger.valueOf(5L)));
        innerInernalList.add(new CLValueU256(BigInteger.valueOf(6L)));
        CLValueList innerList2 = new CLValueList(innerInernalList);

        List<CLValueList> internalOutList = new ArrayList<>();
        internalOutList.add(innerList1);
        internalOutList.add(innerList2);
        CLValueList outerList = new CLValueList(internalOutList);

        final SerializerBuffer ser = new SerializerBuffer();
        outerList.serialize(ser, Target.BYTE);

        byte[] actualBytes = ser.toByteArray();
        assertThat(actualBytes, is(expectedBytes));

        CLValueList clValueList = (CLValueList) outerList.deserialize(new DeserializerBuffer(actualBytes), Target.BYTE);

        assertThat(clValueList.getBytes(), is(outerList.getBytes()));
    }
}
