package com.casper.sdk.model.transaction.scheduling;

import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * No special scheduling applied.
 *
 * @author ian@meywood.com
 */
@NoArgsConstructor
@Builder
@Setter
public class Standard implements TransactionScheduling {
}
