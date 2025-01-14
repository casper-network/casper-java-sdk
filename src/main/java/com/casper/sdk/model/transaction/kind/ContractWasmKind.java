package com.casper.sdk.model.transaction.kind;

import com.casper.sdk.model.contract.ContractWasm;
import com.casper.sdk.model.storedvalue.StoredValue;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * A StoredValue containing a ContractWasm.
 *
 * @author ian@meywood.com
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ContractWasmKind implements StoredValue<ContractWasm> {

    @JsonProperty("ContractWasm")
    private ContractWasm value;

    @Override
    public ContractWasm getValue() {
        return value;
    }
}
