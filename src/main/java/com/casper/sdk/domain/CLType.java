package com.casper.sdk.domain;

/**
 * Domain type: enumeration over set of low level CL types.
 */
public enum CLType {

    /** boolean primitive */
    BOOL(0, "Bool"),
    /** signed 32-bit integer primitive */
    I32(1, "I32"),
    /** signed 64-bit integer primitive */
    I64(2, "I64"),
    /** unsigned 8-bit integer primitive */
    U8(3, "U8"),
    /** unsigned 32-bit integer primitive */
    U32(4, "U32"),
    /** unsigned 64-bit integer primitive */
    U64(5, "U64"),
    /** unsigned 128-bit integer primitive */
    U128(6, "U128"),
    /** unsigned 256-bit integer primitive */
    U256(7, "U256"),
    /** unsigned 512-bit integer primitive */
    U512(8, "U512"),
    /** singleton value without additional semantics */
    UNIT(9, "Unit"),
    /** e.g. "Hello, World!" */
    STRING(10, "String"),
    /** global state key */
    KEY(11, "Key"),
    /** unforgeable reference */
    UREF(12, "URef"),
    /** optional value of the given type Option(CLType) */
    OPTION(13, "Option"),
    /** List of values of the given type (e.g. Vec in rust). List(CLType) */
    LIST(14, "List"),
    /** FIXME NO DEF IN SPEC https://docs.casperlabs.io/en/latest/implementation/serialization-standard.html */
    BYTE_ARRAY(15, "ByteArray"),
    /** co-product of the the given types; one variant meaning success, the other failure */
    RESULT(16, "Result"),
    /** Map(CLType, CLType), // key-value association where keys and values have the given types */
    MAP(17, "Map"),
    /** Tuple1(CLType) single value of the given type */
    TUPLE_1(18, "Tuple1"),
    /** Tuple2(CLType, CLType), // pair consisting of elements of the given types */
    TUPLE_2(19, "Tuple2"),
    /** Tuple3(CLType, CLType, CLType), // triple consisting of elements of the given types */
    TUPLE_3(20, "Tuple3"),
    /** Indicates the type is not known */
    ANY(21, "Any"),
    /** FIXME NO DEF IN SPEC https://docs.casperlabs.io/en/latest/implementation/serialization-standard.html */
    PUBLIC_KEY(22, "PublicKey");

    /** The numeric value for the CL type */
    private final int clType;
    /** The name of the type when written to JSON */
    private final String jsonName;

    CLType(final int clType, final String jsonName) {
        this.clType = clType;
        this.jsonName = jsonName;
    }

    public static CLType fromString(String jsonName) {

        for (CLType clType : values()) {
            if (clType.jsonName.equals(jsonName)) {
                return clType;
            }
        }
        throw new IllegalArgumentException("Not a valid CLType json name " + jsonName);
    }

    public static boolean isNumeric(final CLType clType) {
        return switch (clType) {
            case I32, I64, U8, U32, U64, U128, U256, U512 -> true;
            default -> false;
        };
    }

    public int getClType() {
        return clType;
    }

    public String getJsonName() {
        return jsonName;
    }
}