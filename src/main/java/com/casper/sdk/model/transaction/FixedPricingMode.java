package com.casper.sdk.model.transaction;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The cost of the transaction is determined by the cost table, per the transaction category.
 *
 * @author ian@meywood.com
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class FixedPricingMode implements PricingMode {
    /**
     * User-specified gas_price tolerance (minimum 1).
     * This is interpreted to mean "do not include this transaction in a block
     * if the current gas price is greater than this number"
     */
    @JsonProperty("gas_price_tolerance")
    private byte gasPriceTolerance;
}
