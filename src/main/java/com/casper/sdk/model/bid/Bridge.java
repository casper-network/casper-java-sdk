package com.casper.sdk.model.bid;

import com.casper.sdk.model.key.PublicKey;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Bridge record pointing to a new `ValidatorBid` after the public key was changed.
 *
 * @author ian@meywood.com
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Bridge implements BidKind {
    /** Previous validator public key associated with the bid. */
    @JsonProperty("old_validator_public_key")
    private PublicKey oldValidatorPublicKey;
    /** New validator public key associated with the bid. */
    @JsonProperty("new_validator_public_key")
    private PublicKey newValidatorPublicKey;
    /** Era when bridge record was created. */
    @JsonProperty("era_id")
    private long eraId;
}
