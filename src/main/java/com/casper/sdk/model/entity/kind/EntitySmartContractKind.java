package com.casper.sdk.model.entity.kind;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
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
public class EntitySmartContractKind implements EntityAddressKind {

    @JsonCreator
    public static EntitySmartContractKind create(final TransactionRuntime value) {
        return new EntitySmartContractKind(value);
    }

    /** The runtime used to execute a Transaction. */
    public enum TransactionRuntime {
        @JsonProperty("VmCasperV1")
        VMCASPERV1,
        @JsonProperty("VmCasperV2")
        VMCASPERV2
    }

    @JsonValue
    private TransactionRuntime smartContract;

}
