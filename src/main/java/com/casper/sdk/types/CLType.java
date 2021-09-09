package com.casper.sdk.types;

import com.casper.sdk.exceptions.ConversionException;

/**
 * Type: enumeration over set of low level CL types. {@see https://docs.casperlabs.io/en/latest/implementation/serialization-standard.html#values}
 */
public enum CLType {

    /** boolean primitive */
    @CLName("Bool")
    BOOL(0),
    /** signed 32-bit integer primitive */
    I32(1),
    /** signed 64-bit integer primitive */
    I64(2),
    /** unsigned 8-bit integer primitive */
    U8(3),
    /** unsigned 32-bit integer primitive */
    U32(4),
    /** unsigned 64-bit integer primitive */
    U64(5),
    /** unsigned 128-bit integer primitive */
    U128(6),
    /** unsigned 256-bit integer primitive */
    U256(7),
    /** unsigned 512-bit integer primitive */
    U512(8),
    /** singleton value without additional semantics */
    @CLName("Unit")
    UNIT(9),
    /** e.g. "Hello, World!" */
    @CLName("String")
    STRING(10),
    /** global state key */
    @CLName("Key")
    KEY(11),
    /** unforgeable reference */
    @CLName("URef")
    UREF(12),
    /** optional value of the given type Option(CLType) */
    @CLName("Option")
    OPTION(13),
    /** List of values of the given type (e.g. Vec in rust). List(CLType) */
    @CLName("List")
    LIST(14),
    /** Byte array prefixed with U32 length (FixedList) */
    @CLName("ByteArray")
    BYTE_ARRAY(15),
    /** co-product of the the given types; one variant meaning success, the other failure */
    @CLName("Result")
    RESULT(16),
    /** Map(CLType, CLType), // key-value association where keys and values have the given types */
    @CLName("Map")
    MAP(17),
    /** Tuple1(CLType) single value of the given type */
    @CLName("Tuple1")
    TUPLE_1(18),
    /** Tuple2(CLType, CLType), // pair consisting of elements of the given types */
    @CLName("Tuple2")
    TUPLE_2(19),
    /** Tuple3(CLType, CLType, CLType), // triple consisting of elements of the given types */
    @CLName("Tuple3")
    TUPLE_3(20),
    /** Indicates the type is not known */
    @CLName("Any")
    ANY(21),
    /** NO DEF IN SPEC https://docs.casperlabs.io/en/latest/implementation/serialization-standard.html */
    @CLName("PublicKey")
    PUBLIC_KEY(22);

    /** The numeric value for the CL type */
    private final int clType;

    CLType(final int clType) {
        this.clType = clType;
    }

    public static CLType fromString(String jsonName) {

        for (CLType clType : values()) {
            if (clType.getJsonName().equals(jsonName)) {
                return clType;
            }
        }
        throw new IllegalArgumentException("Not a valid CLType json name " + jsonName);
    }

    public static boolean isNumeric(final CLType clType) {
        switch (clType) {
            case I32:
            case I64:
            case U8:
            case U32:
            case U64:
            case U128:
            case U256:
            case U512:
                return true;

            default:
                return false;
        }
    }

    public byte getClType() {
        return (byte) clType;
    }

    /**
     * Obtains the JSON name of the field. If a {@link CLName} annotation exists on the CLType returns its value
     * otherwise the name.
     *
     * @return the JSON name of the CLType
     */
    public String getJsonName() {
        try {
            final CLName annotation = this.getClass()
                    .getField(this.name())
                    .getAnnotation(CLName.class);
            if (annotation != null) {
                return annotation.value();
            } else {
                return name();
            }
        } catch (NoSuchFieldException e) {
            // Ignore should never happen
            throw new ConversionException(e);
        }
    }
}
