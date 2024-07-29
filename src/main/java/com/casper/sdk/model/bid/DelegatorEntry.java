package com.casper.sdk.model.bid;

import com.casper.sdk.model.key.PublicKey;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents a party delegating their stake to a validator (or "delegatee")
 *
 * @author ian@meywood.com
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DelegatorEntry {

    @JsonProperty("delegator_public_key")
    private PublicKey delegatorPublicKey;

    private JsonDelegator delegator;

}
