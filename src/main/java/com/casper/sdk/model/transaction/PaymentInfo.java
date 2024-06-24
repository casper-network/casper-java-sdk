package com.casper.sdk.model.transaction;

import com.casper.sdk.model.uref.URef;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Breakdown of payments made to cover the cost.
 *
 * @author ian@meywood.com
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PaymentInfo {
    /** Source purse used for payment of the transaction. */
    public URef source;
}
