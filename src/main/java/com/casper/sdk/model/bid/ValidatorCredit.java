package com.casper.sdk.model.bid;

import com.casper.sdk.model.key.PublicKey;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;

/**
 * Validator credit record.
 *
 * @author ian@meywood.com
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ValidatorCredit implements BidKind {
    /** Validator public key */
    @JsonProperty("validator_public_key")
    private PublicKey validatorPublicKey;
    /** The era id the credit was created. */
    @JsonProperty("era_id")
    private long eraId;
    /** The credit amount. */
    @JsonProperty("amount")
    private BigInteger amount;
}
