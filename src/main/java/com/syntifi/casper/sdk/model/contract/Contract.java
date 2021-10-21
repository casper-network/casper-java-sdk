package com.syntifi.casper.sdk.model.contract;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * Methods and type signatures supported by a contract.
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
@Data
public class Contract {

    /**
     * contract_package_hash(String) - The hash address of the contract package.
     */
    @JsonProperty("contract_package_hash")
    private String packageHash;
    
    /**
     * contract_wasm_hash(String) The hash address of the contract wasm.
     */
    @JsonProperty("contract_wasm_hash")
    private String wasmHash;

    /**
     * entry_points(Array) - A list of entry points.
     */
    @JsonProperty("entry_points")
    private List<EntryPoint> entryPoints;

    /**
     * named_keys(Array) - A list of named keys.
     */
    @JsonProperty("named_keys")
    private List<NamedKey> namedKeys;

    /**
     * protocol_version(String) - ?
     */
    @JsonProperty("protocol_version")
    private String protocolVersion;
}
