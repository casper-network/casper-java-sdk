package com.syntifi.casper.sdk.model.storedvalue;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.syntifi.casper.sdk.model.contract.ContractPackage;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Stored Value for {@link ContractPackage}
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @see StoredValue
 * @since 0.0.1
 */
@Getter
@Setter
@Builder
@JsonTypeName("ContractPackage")
public class StoredValueContractPackage implements StoredValue<ContractPackage> {
    @JsonProperty("ContractPackage")
    public ContractPackage value;
}
