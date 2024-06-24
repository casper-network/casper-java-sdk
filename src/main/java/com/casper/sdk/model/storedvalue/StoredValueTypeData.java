package com.casper.sdk.model.storedvalue;

import com.casper.sdk.exception.NoSuchTypeException;
import com.casper.sdk.model.bid.StoredValueBidKind;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Stored Value type data and class mapping
 *
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @see StoredValue
 * @since 0.0.1
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum StoredValueTypeData {
    STORED_VALUE_CLVALUE("CLValue", StoredValueCLValue.class),
    STORED_VALUE_ACCOUNT("Account", StoredValueAccount.class),
    STORED_VALUE_CONTRACT_PACKAGE("ContractPackage", StoredValueContractPackage.class),
    STORED_VALUE_CONTRACT("Contract", StoredValueContract.class),
    STORED_VALUE_TRANSFER("Transfer", StoredValueTransfer.class),
    STORED_VALUE_DEPLOY_INFO("DeployInfo", StoredValueDeployInfo.class),
    STORED_VALUE_ERA_INFO("EraInfo", StoredValueEraInfo.class),
    STORED_VALUE_BID("Bid", StoredValueBid.class),
    STORED_VALUE_BID_KIND("BidKind", StoredValueBidKind.class),
    STORED_VALUE_WITHDRAW("Withdraw", StoredValueWithdraw.class);

    private final String name;
    private final Class<?> clazz;

    /**
     * Retrieve Transform implementation class from Transform name
     *
     * @param name the name to use for fetching class
     * @return the class object for given name
     * @throws NoSuchTypeException thrown if class type not found
     */
    public static Class<?> getClassByName(String name) throws NoSuchTypeException {
        for (StoredValueTypeData t : values()) {
            if (t.name.equals(name)) {
                return t.getClazz();
            }
        }
        throw new NoSuchTypeException();
    }
}
