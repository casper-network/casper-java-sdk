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
        @JsonSubTypes.Type(value = PaymentLimited.class, name = "PaymentLimited"),
        @JsonSubTypes.Type(value = FixedPricingMode.class, name = "Fixed"),
        @JsonSubTypes.Type(value = PrepaidPricingMode.class, name = "Prepaid"),})
public interface PricingMode extends CasperSerializableObject, Tag {

    int PAYMENT_LIMITED_TAG = 0;
    int FIXED_TAG = 1;
    int PREPAID_TAG = 2;
    int TAG_FIELD_INDEX = 0;
}
