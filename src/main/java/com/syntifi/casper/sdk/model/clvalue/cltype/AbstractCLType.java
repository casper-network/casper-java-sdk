package com.syntifi.casper.sdk.model.clvalue.cltype;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonTypeResolver;
import com.syntifi.casper.sdk.exception.NoSuchTypeException;
import com.syntifi.casper.sdk.jackson.resolver.CLTypeResolver;

import lombok.Getter;
import lombok.Setter;

/**
 * Basic class for CLType implementation
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @see AbstractCLType
 * @since 0.0.1
 */
@Getter
@Setter
@JsonTypeInfo(use = JsonTypeInfo.Id.NONE)
@JsonTypeResolver(CLTypeResolver.class)
public abstract class AbstractCLType {
    /**
     * String constants for all CLTypes as defined by the api spec
     */
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

    /**
     * Required getter for implementations of CLType
     * 
     * @return the CLType name
     */
    @JsonIgnore
    public abstract String getTypeName();

    /**
     * @return the {@link CLTypeData} for the current CLType
     * @throws NoSuchTypeException
     */
    @JsonIgnore
    public CLTypeData getClTypeData() throws NoSuchTypeException {
        return CLTypeData.getTypeByName(getTypeName());
    }
}
