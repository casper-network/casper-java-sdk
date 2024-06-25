package com.casper.sdk.model.transaction.pricing;

import com.casper.sdk.model.common.Digest;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The payment for this transaction was previously reserved, as proven by
 * the receipt hash (this is for future use, not currently implemented).
 *
 * @author ian@meywood.com
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ReservedPricingMode implements PricingMode {
    @JsonProperty("receipt")
    private Digest receipt;
}
