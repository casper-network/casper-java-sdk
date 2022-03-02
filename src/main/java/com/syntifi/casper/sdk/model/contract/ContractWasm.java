package com.syntifi.casper.sdk.model.contract;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * A contract's Wasm.
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
@Getter
@Setter
@Builder
public class ContractWasm {

    /**
     * ContractWasm(object/string) A contract's Wasm.
     */
    @JsonProperty("ContractWasm")
    private String wasm;
}
