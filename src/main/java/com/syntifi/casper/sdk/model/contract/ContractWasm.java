package com.syntifi.casper.sdk.model.contract;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * A contract's Wasm.
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
@Data
public class ContractWasm {

    /**
     * ContractWasm(object/string) A contract's Wasm.
     */
    @JsonProperty("ContractWasm")
    private String wasm;
}
