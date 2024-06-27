package com.casper.sdk.model.transaction.target;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.*;

/**
 * The execution target is the included module bytes, i.e. compiled Wasm.
 *
 * @author ian@meywood.com
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@JsonTypeName("Session")
public class Session implements TransactionTarget {

    /** The compiled Wasm. */
    @JsonProperty("module_bytes")
    private byte[] moduleBytes;
    /** The execution runtime to use. */
    @JsonProperty("runtime")
    private TransactionRuntime runtime;
}
