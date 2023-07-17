package com.casper.sdk.model.clvalue.cltype;

import com.casper.sdk.exception.DynamicInstanceException;
import com.casper.sdk.exception.NoSuchTypeException;
import com.casper.sdk.model.clvalue.*;
import com.casper.sdk.model.storedvalue.StoredValue;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.lang.reflect.InvocationTargetException;

/**
 * Casper CLType definitions and type mappings
 * <p>
 * All types must be listed and mapped here.
 *
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @see StoredValue
 * @since 0.0.1
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum CLTypeData {
    BOOL(AbstractCLType.BOOL, (byte) 0x0, CLValueBool.class, CLTypeBool.class),
    I32(AbstractCLType.I32, (byte) 0x1, CLValueI32.class, CLTypeI32.class),
    I64(AbstractCLType.I64, (byte) 0x2, CLValueI64.class, CLTypeI64.class),
    U8(AbstractCLType.U8, (byte) 0x3, CLValueU8.class, CLTypeU8.class),
    U32(AbstractCLType.U32, (byte) 0x4, CLValueU32.class, CLTypeU32.class),
    U64(AbstractCLType.U64, (byte) 0x5, CLValueU64.class, CLTypeU64.class),
    U128(AbstractCLType.U128, (byte) 0x6, CLValueU128.class, CLTypeU128.class),
    U256(AbstractCLType.U256, (byte) 0x7, CLValueU256.class, CLTypeU256.class),
    U512(AbstractCLType.U512, (byte) 0x8, CLValueU512.class, CLTypeU512.class),
    UNIT(AbstractCLType.UNIT, (byte) 0x9, CLValueUnit.class, CLTypeUnit.class),
    STRING(AbstractCLType.STRING, (byte) 0xA, CLValueString.class, CLTypeString.class),
    KEY(AbstractCLType.KEY, (byte) 0xB, CLValueKey.class, CLTypeKey.class),
    UREF(AbstractCLType.UREF, (byte) 0xC, CLValueURef.class, CLTypeURef.class),
    OPTION(AbstractCLType.OPTION, (byte) 0xD, CLValueOption.class, CLTypeOption.class),
    LIST(AbstractCLType.LIST, (byte) 0xE, CLValueList.class, CLTypeList.class),
    BYTE_ARRAY(AbstractCLType.BYTE_ARRAY, (byte) 0xF, CLValueByteArray.class, CLTypeByteArray.class),
    RESULT(AbstractCLType.RESULT, (byte) 0x10, CLValueResult.class, CLTypeResult.class),
    MAP(AbstractCLType.MAP, (byte) 0x11, CLValueMap.class, CLTypeMap.class),
    TUPLE1(AbstractCLType.TUPLE1, (byte) 0x12, CLValueTuple1.class, CLTypeTuple1.class),
    TUPLE2(AbstractCLType.TUPLE2, (byte) 0x13, CLValueTuple2.class, CLTypeTuple2.class),
    TUPLE3(AbstractCLType.TUPLE3, (byte) 0x14, CLValueTuple3.class, CLTypeTuple3.class),
    ANY(AbstractCLType.ANY, (byte) 0x15, CLValueAny.class, CLTypeAny.class),
    PUBLIC_KEY(AbstractCLType.PUBLIC_KEY, (byte) 0x16, CLValuePublicKey.class, CLTypePublicKey.class);

    private final String clTypeName;
    private final byte serializationTag;
    private final Class<? extends AbstractCLValue<?, ?>> clazz;
    private final Class<? extends AbstractCLType> clTypeClass;

    /**
     * Retrieve CLType by its serialization tag
     *
     * @param serializationTag the serialization tag to find
     * @return the requested {@link CLTypeData}
     * @throws NoSuchTypeException raised when the clType is not valid/found
     */
    public static CLTypeData getTypeBySerializationTag(byte serializationTag) throws NoSuchTypeException {
        for (CLTypeData clType : values()) {
            if (clType.serializationTag == serializationTag) {
                return clType;
            }
        }
        throw new NoSuchTypeException();
    }

    /**
     * Retrieve CLValue implementation class from CLType name
     *
     * @param name the type's name
     * @return the {@link Class} object holding the requested
     * {@link AbstractCLValue}
     * @throws NoSuchTypeException raised when the clType is not valid/found
     */
    public static Class<?> getClassByName(String name) throws NoSuchTypeException {
        for (CLTypeData clType : values()) {
            if (clType.clTypeName.equals(name)) {
                return clType.getClazz();
            }
        }
        throw new NoSuchTypeException();
    }

    /**
     * Retrieve CLType class from CLType name
     *
     * @param name the type's name
     * @return the {@link Class} object holding the requested {@link AbstractCLType}
     * @throws NoSuchTypeException raised when the clType is not valid/found
     */
    public static Class<?> getCLTypeClassByName(String name) throws NoSuchTypeException {
        for (CLTypeData clType : values()) {
            if (clType.clTypeName.equals(name)) {
                return clType.getClTypeClass();
            }
        }
        throw new NoSuchTypeException();
    }

    /**
     * Retrieve CLType from its name
     *
     * @param name the type's name
     * @return the requested {@link CLTypeData}
     * @throws NoSuchTypeException raised when the clType is not valid/found
     */
    public static CLTypeData getTypeByName(String name) throws NoSuchTypeException {
        for (CLTypeData clType : values()) {
            if (clType.clTypeName.equals(name)) {
                return clType;
            }
        }
        throw new NoSuchTypeException();
    }

    /**
     * Dynamically instantiate a CLValue when needed for decoding children objects
     *
     * @param clValueName the name of the {@link AbstractCLValue} to instantiate
     * @return the desired {@link AbstractCLValue} implementation
     * @throws DynamicInstanceException error while dynamically instantiating the
     *                                  clValue
     * @throws NoSuchTypeException      raised when the clType is not valid/found
     */
    public static AbstractCLValue<?, ?> createCLValueFromCLTypeName(String clValueName)
            throws DynamicInstanceException, NoSuchTypeException {
        return CLTypeData.createCLValueFromCLTypeData(CLTypeData.getTypeByName(clValueName));
    }

    /**
     * Dynamically instantiate a CLValue when needed for decoding children objects
     *
     * @param clTypeData the {@link CLTypeData} to instantiate
     * @return the desired {@link AbstractCLValue} implementation
     * @throws DynamicInstanceException error while dynamically instantiating the
     *                                  clValue
     */
    public static AbstractCLValue<?, ?> createCLValueFromCLTypeData(CLTypeData clTypeData)
            throws DynamicInstanceException {
        Class<?> clazz = clTypeData.getClazz();

        try {
            return (AbstractCLValue<?, ?>) clazz.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                | NoSuchMethodException | SecurityException e) {
            throw new DynamicInstanceException(String.format("Error while instantiating %s", clazz.getName()), e);
        }
    }

    /**
     * Dynamically instantiate a CLType when needed for decoding children objects
     *
     * @param clTypeName the name of the {@link AbstractCLType} to instantiate
     * @return the desired {@link AbstractCLType} implementation
     * @throws DynamicInstanceException error while dynamically instantiating the
     *                                  clValue
     * @throws NoSuchTypeException      raised when the clType is not valid/found
     */
    public static AbstractCLType createCLTypeFromCLTypeName(String clTypeName)
            throws DynamicInstanceException, NoSuchTypeException {
        return CLTypeData.createCLTypeFromCLTypeData(CLTypeData.getTypeByName(clTypeName));
    }

    /**
     * Dynamically instantiate a CLType when needed for decoding children objects
     *
     * @param clTypeData the {@link CLTypeData} to instantiate
     * @return the desired {@link AbstractCLType} implementation
     * @throws DynamicInstanceException error while dynamically instantiating the
     *                                  clValue
     */
    public static AbstractCLType createCLTypeFromCLTypeData(CLTypeData clTypeData) throws DynamicInstanceException {
        Class<?> clazz = clTypeData.getClTypeClass();

        try {
            return (AbstractCLType) clazz.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                | NoSuchMethodException | SecurityException e) {
            throw new DynamicInstanceException(String.format("Error while instantiating %s", clazz.getName()), e);
        }
    }
}
