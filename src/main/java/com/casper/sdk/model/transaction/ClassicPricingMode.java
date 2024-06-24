package com.casper.sdk.model.transaction;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;

/**
 * The original payment model, where the creator of the transaction specifies how much they will pay, at what gas price.
 *
 * @author ian@meywood.com
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ClassicPricingMode implements PricingMode {
    /** User-specified payment amount. */
    @JsonProperty("payment_amount")
    private BigInteger paymentAmount;
    /**
     * User-specified gas_price tolerance (minimum 1). This is interpreted to mean "do not include this transaction in
     * a block if the current gas price is greater than this number"
     */
    @JsonProperty("gas_price_tolerance")
    private byte gasPriceTolerance;
    /** Standard payment. */
    @JsonProperty("standard_payment")
    boolean standardPayment;
}
