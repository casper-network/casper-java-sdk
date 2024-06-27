package com.casper.sdk.model.transaction.target;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The runtime used to execute a `Transaction`.
 *
 * @author ian@meywood.com
 */
public enum TransactionRuntime {
    /** The Casper Version 1 Virtual Machine. */
    @JsonProperty("VmCasperV1")
    VM_CASPER_V1,
    /** The Casper Version 2 Virtual Machine. */
    @JsonProperty("VmCasperV2")
    VM_CASPER_V2
}
