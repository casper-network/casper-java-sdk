package com.casper.sdk.model.bid;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Interface for the different types of bids since Condor.
 *
 * @author ian@meywood.com
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Bid.class, name = "Unified"),
        @JsonSubTypes.Type(value = ValidatorBid.class, name = "Validator"),
        @JsonSubTypes.Type(value = Delegator.class, name = "Delegator"),
        @JsonSubTypes.Type(value = Bridge.class, name = "Bridge"),
        @JsonSubTypes.Type(value = ValidatorCredit.class, name = "Credit")
})
public interface BidKind {

}

