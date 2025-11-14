package com.casper.sdk.model.entity.contract;

import com.casper.sdk.model.contract.ContractWasm;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author ian@meywood.com
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Wasm {
    private ContractWasm wasm;
    @JsonProperty("merkle_proof")
    private String merkleProof;
}
