package com.casper.sdk.model.entity;

import com.casper.sdk.model.contract.Contract;
import com.casper.sdk.model.entity.contract.Wasm;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Contract entity
 *
 * @author ian@meywood.com
 */
@NoArgsConstructor
@JsonTypeName("Contract")
@Getter
@Setter
public class ContractEntity implements StateEntity {

    private Contract contract;
    private Wasm wasm;
}
