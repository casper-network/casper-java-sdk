package com.syntifi.casper.sdk.model.storedvalue.clvalue;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * All CLTypes must be listed and consumed here
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @see CLType
 * @see CLTypeData
 * @since 0.0.1
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CLType {
    public static final String BOOL = "Bool";
    public static final String I32 = "I32";
    public static final String I64 = "I64";
    public static final String U8 = "U8";
    public static final String U32 = "U32";
    public static final String U64 = "U64";
    public static final String U128 = "U128";
    public static final String U256 = "U256";
    public static final String U512 = "U512";
    public static final String UNIT = "Unit";
    public static final String STRING = "String";
    public static final String KEY = "Key";
    public static final String UREF = "URef";
    public static final String OPTION = "Option";
    public static final String LIST = "List";
    public static final String FIXED_LIST = "FixedList";
    public static final String RESULT = "Result";
    public static final String MAP = "Map";
    public static final String TUPLE1 = "Tuple1";
    public static final String TUPLE2 = "Tuple2";
    public static final String TUPLE3 = "Tuple3";
    public static final String ANY = "Any";
    public static final String PUBLIC_KEY = "PublicKey";
    public static final String BYTE_ARRAY = "ByteArray";
    
    private CLTypeData clTypeData;
    private List<CLType> childTypes;
}
