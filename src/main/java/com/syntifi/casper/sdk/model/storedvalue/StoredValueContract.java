package com.syntifi.casper.sdk.model.storedvalue;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.syntifi.casper.sdk.model.contract.Contract;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Stored Value for {@link Contract}
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @see StoredValue
 * @since 0.0.1
 */
@Getter
@Setter
@Builder
@JsonTypeName("Contract")
public class StoredValueContract implements StoredValue<Contract> {
    @JsonProperty("Contract")
    public Contract value;
}
