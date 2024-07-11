package com.casper.sdk.model.entity;

import com.fasterxml.jackson.annotation.*;
import lombok.*;

/**
 * Packages associated with Wasm stored on chain.
 *
 * @author carl@stormeye.co.uk
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SmartContract implements EntityAddressKind {

    @JsonCreator
    public static SmartContract create(final TransactionRuntime value){
        return new SmartContract(value);
    }

    public enum TransactionRuntime {
        @JsonProperty("VmCasperV1")
        VMCASPERV1,
        @JsonProperty("VmCasperV2")
        VMCASPERV2
    }

    @JsonValue
    private TransactionRuntime smartContract;

}
