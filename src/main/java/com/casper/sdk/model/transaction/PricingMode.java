package com.casper.sdk.model.transaction;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * The pricing mode for a transaction.
 *
 * @author ian@meywood.com
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
@JsonSubTypes({
        @JsonSubTypes.Type(value = ClassicPricingMode.class, name = "Classic"),
        @JsonSubTypes.Type(value = FixedPricingMode.class, name = "Fixed"),
        @JsonSubTypes.Type(value = ReservedPricingMode.class, name = "Reserved"),})
public interface PricingMode {
}
