package com.syntifi.casper.sdk.model.storedvalue.clvalue;

import java.lang.reflect.InvocationTargetException;

import com.syntifi.casper.sdk.exception.DynamicInstanceException;
import com.syntifi.casper.sdk.exception.NoSuchCLTypeException;
import com.syntifi.casper.sdk.model.storedvalue.StoredValue;

import lombok.Getter;

/**
 * Casper CLType definitions and type mappings
 * 
 * All types must be listed and mapped here.
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @see StoredValue
 * @since 0.0.1
 */
@Getter
public enum CLTypeData {
    // TODO: Register and check if all types are present and fix identifier type and
    // value
    BOOL(CLType.BOOL, 0x0, CLValueBool.class), 
    I32(CLType.I32, 0x1, CLValueI32.class),
    I64(CLType.I64, 0x2, CLValueI64.class), 
    U8(CLType.U8, 0x3, CLValueU8.class), 
    U32(CLType.U32, 0x4, CLValueU32.class),
    U64(CLType.U64, 0x5, CLValueU64.class), 
    U128(CLType.U128, 0x6, CLValueU128.class),
    U256(CLType.U256, 0x7, CLValueU256.class), 
    U512(CLType.U512, 0x8, CLValueU512.class),
    UNIT(CLType.UNIT, 0x9, CLValueUnit.class), 
    STRING(CLType.STRING, 0x10, CLValueString.class),
    UREF(CLType.UREF, 0x11, CLValueURef.class),
    KEY(CLType.KEY, 0x12, null),
    OPTION(CLType.OPTION, 0x13, CLValueOption.class), 
    LIST(CLType.LIST, 0x14, CLValueList.class),
    FIXED_LIST(CLType.FIXED_LIST, 0x15, null), 
    RESULT(CLType.RESULT, 0x16, CLValueResult.class),
    MAP(CLType.MAP, 0x17, CLValueMap.class), 
    TUPLE1(CLType.TUPLE1, 0x18, CLValueTuple1.class),
    TUPLE2(CLType.TUPLE2, 0x19, CLValueTuple2.class), 
    TUPLE3(CLType.TUPLE3, 0x20, CLValueTuple3.class),
    ANY(CLType.ANY, 0x21, CLValueAny.class), 
    PUBLIC_KEY(CLType.PUBLIC_KEY, 0x22, CLValuePublicKey.class),
    BYTE_ARRAY(CLType.BYTE_ARRAY, 0x23, CLValueByteArray.class);

    private final String name;
    private final int serializationTag;
    private final Class<?> clazz;

    private CLTypeData(String name, int serializationTag, Class<?> clazz) {
        this.name = name;
        this.serializationTag = serializationTag;
        this.clazz = clazz;
    }

    /**
     * Retrieve CLType by its serialization tag
     * 
     * @param serializationTag
     * @return
     * @throws NoSuchCLTypeException
     */
    public static CLTypeData getTypeBySerializationTag(byte serializationTag) throws NoSuchCLTypeException {
        for (CLTypeData clType : values()) {
            if (clType.serializationTag == serializationTag) {
                return clType;
            }
        }
        throw new NoSuchCLTypeException();
    }

    /**
     * Retrieve CLValue implementation class from CLType name
     * 
     * @param name
     * @return
     * @throws NoSuchCLTypeException
     */
    public static Class<?> getClassByName(String name) throws NoSuchCLTypeException {
        for (CLTypeData clType : values()) {
            if (clType.name.equals(name)) {
                return clType.getClazz();
            }
        }
        throw new NoSuchCLTypeException();
    }

    /**
     * Retrieve CLType from its name
     * 
     * @param name
     * @return
     * @throws NoSuchCLTypeException
     */
    public static CLTypeData getTypeByName(String name) throws NoSuchCLTypeException {
        for (CLTypeData clType : values()) {
            if (clType.name.equals(name)) {
                return clType;
            }
        }
        throw new NoSuchCLTypeException();
    }

    /**
     * Dynamically instantiate an AbstractCLValue when needed for decoding children objects
     * 
     * @param clTypeData the {@link CLTypeData} to instantiate
     * @return the desired CLValue implementation
     * @throws DynamicInstanceException
     */
    public static AbstractCLValue<?> createCLValueFromCLTypeData(CLTypeData clTypeData) throws DynamicInstanceException {
        Class<?> clazz = clTypeData.getClazz();

        try {
            return (AbstractCLValue<?>) clazz.getConstructor().newInstance();

        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                | NoSuchMethodException | SecurityException e) {
            throw new DynamicInstanceException(String.format("Error while instantiating %s", clazz.getName()), e);
        }
    }
}
