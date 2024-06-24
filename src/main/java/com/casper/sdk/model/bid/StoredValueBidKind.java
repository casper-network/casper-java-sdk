package com.casper.sdk.model.bid;

import com.casper.sdk.model.storedvalue.StoredValue;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.*;

/**
 * Represents a stored value of BidKind.
 *
 * @author ian@meywood.com
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonTypeName("BidKind")
public class StoredValueBidKind implements StoredValue<BidKind> {

    @JsonProperty("BidKind")
    private BidKind value;
}
