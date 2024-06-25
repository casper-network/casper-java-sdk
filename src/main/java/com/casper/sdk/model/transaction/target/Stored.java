package com.casper.sdk.model.transaction.target;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.*;

/**
 * The execution target is a stored entity or package.
 *
 * @author ian@meywood.com
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@JsonTypeName("Stored")
public class Stored implements TransactionTarget {

    /** The execution runtime to use. */
    @JsonProperty("runtime")
    private TransactionRuntime runtime;
}
