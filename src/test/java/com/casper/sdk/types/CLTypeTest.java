package com.casper.sdk.types;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Unit test for the {@link CLType}
 */
class CLTypeTest {

    @Test
    void testClTypeLength() {
        assertThat(CLType.values().length, is(23));
    }

    @Test
    void testClTypeBool() {
        assertClType(CLType.BOOL, 0, "Bool", false);
    }

    @Test
    void testClTypeI32() {
        assertClType(CLType.I32, 1, "I32", true);
    }

    @Test
    void testClTypeI64() {
        assertClType(CLType.I64, 2, "I64", true);
    }

    @Test
    void testClTypeU8() {
        assertClType(CLType.U8, 3, "U8", true);
    }

    @Test
    void testClTypeU32() {
        assertClType(CLType.U32, 4, "U32", true);
    }

    @Test
    void testClTypeU64() {
        assertClType(CLType.U64, 5, "U64", true);
    }

    @Test
    void testClTypeU128() {
        assertClType(CLType.U128, 6, "U128", true);
    }

    @Test
    void testClTypeU256() {
        assertClType(CLType.U256, 7, "U256", true);
    }

    @Test
    void testClTypeU512() {
        assertClType(CLType.U512, 8, "U512", true);
    }

    @Test
    void testClTypeUnit() {
        assertClType(CLType.UNIT, 9, "Unit", false);
    }

    @Test
    void testClTypeString() {
        assertClType(CLType.STRING, 10, "String", false);
    }

    @Test
    void testClTypeKey() {
        assertClType(CLType.KEY, 11, "Key", false);
    }

    @Test
    void testClTypeURef() {
        assertClType(CLType.UREF, 12, "URef", false);
    }

    @Test
    void testClTypeOption() {
        assertClType(CLType.OPTION, 13, "Option", false);
    }

    @Test
    void testClTypeList() {
        assertClType(CLType.LIST, 14, "List", false);
    }

    @Test
    void testClTypeByteArray() {
        assertClType(CLType.BYTE_ARRAY, 15, "ByteArray", false);
    }

    @Test
    void testClTypeResult() {
        assertClType(CLType.RESULT, 16, "Result", false);
    }

    @Test
    void testClTypeMap() {
        assertClType(CLType.MAP, 17, "Map", false);
    }

    @Test
    void testClTypeTuple1() {
        assertClType(CLType.TUPLE_1, 18, "Tuple1", false);
    }

    @Test
    void testClTypeTuple2() {
        assertClType(CLType.TUPLE_2, 19, "Tuple2", false);
    }

    @Test
    void testClTypeTuple3() {
        assertClType(CLType.TUPLE_3, 20, "Tuple3", false);
    }

    @Test
    void testClTypeAny() {
        assertClType(CLType.ANY, 21, "Any", false);
    }

    @Test
    void testClTypePublicKey() {
        assertClType(CLType.PUBLIC_KEY, 22, "PublicKey", false);
    }

    private void assertClType(final CLType type, final int expectedType, final String expectedJson, final boolean expectedNumeric) {
        assertThat(type.getClType(), is((byte)expectedType));
        assertThat(type.getJsonName(), is(expectedJson));
        assertThat(CLType.isNumeric(type), is(expectedNumeric));
    }
}

