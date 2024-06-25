package com.casper.sdk.model.transaction.target;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author ian@meywood.com
 */
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class TransactionInvocationTargetByHash implements TransactionInvocationTarget {

    private HashAddress hashAddress;
}
