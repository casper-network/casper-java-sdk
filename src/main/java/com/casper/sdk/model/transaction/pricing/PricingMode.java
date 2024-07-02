package com.casper.sdk.model.transaction.pricing;

import com.casper.sdk.model.clvalue.serde.CasperSerializableObject;
import com.casper.sdk.model.key.Tag;
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
public interface PricingMode extends CasperSerializableObject, Tag {

    int CLASSIC_TAG = 0;
    int FIXED_TAG = 1;
    int RESERVED_TAG = 2;
}
