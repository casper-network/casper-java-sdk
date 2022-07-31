package com.casper.sdk.model.deploy.transform;

import com.casper.sdk.exception.NoSuchTypeException;
import lombok.Getter;

@Getter
public enum TransformTypeData {
    WRITE_ADDI32("AddInt32", AddInt32.class),
    WRITE_ADDKEYS("AddKeys", AddKeys.class),
    WRITE_ADDU64("AddUInt64", AddUInt64.class),
    WRITE_ADDU128("AddUInt128", AddUInt128.class),
    WRITE_ADDU256("AddUInt256", AddUInt256.class),
    WRITE_ADDU512("AddUInt512", AddUInt512.class),
    FAILURE("Failure", Failure.class),
    WRITE_ACCOUNT("WriteAccount", WriteAccount.class),
    WRITE_BID("WriteBid", WriteBid.class),
    WRITE_CLVALUE("WriteCLValue", WriteCLValue.class),
    ENUM_ID("Identity", WriteContract.class),
    ENUM_CONTR_WASM("WriteContractWasm", WriteContract.class),
    ENUM_CONTR("WriteContract", WriteContract.class),
    ENUM_CONTR_PKG("WriteContractPackage", WriteContract.class),
    WRITE_DEPLOY_INFO("WriteDeployInfo", WriteDeployInfo.class),
    WRITE_ERA_INFO("WriteEraInfo", WriteEraInfo.class),
    WRITE_TRANSFER("WriteTransfer", WriteTransfer.class),
    WRITE_WITHDRAW("WriteWithdraw", WriteWithdraw.class);

    private final String name;
    private final Class<?> clazz;

    TransformTypeData(String name, Class<?> clazz) {
        this.name = name;
        this.clazz = clazz;
    }

    /**
     * Retrieve Transform implementation class from Transform name
     *
     * @param name {@link TransformTypeData} class name
     * @return the class object for the {@link TransformTypeData}
     * @throws NoSuchTypeException if no type is found for given name
     */
    public static Class<?> getClassByName(String name) throws NoSuchTypeException {
        for (TransformTypeData t : values()) {
            if (t.name.equals(name)) {
                return t.getClazz();
            }
        }
        throw new NoSuchTypeException();
    }
}