package com.casper.sdk.model.status;

import lombok.*;

import java.math.BigInteger;

/**
 * An unbroken, inclusive range of blocks
 *
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AvailableBlockRange {
    /**
     * The inclusive lower bound of the range
     */
    BigInteger low;

    /**
     * The inclusive upper bound of the range
     */
    BigInteger high;
}
