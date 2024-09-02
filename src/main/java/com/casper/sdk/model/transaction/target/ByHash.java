package com.casper.sdk.model.transaction.target;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The address identifying the invocable entity.
 *
 * @author ian@meywood.com
 */
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ByHash implements TransactionInvocationTarget {
    @JsonValue
    private HashAddress hashAddress;
}
