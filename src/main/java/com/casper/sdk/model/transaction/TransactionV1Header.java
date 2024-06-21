package com.casper.sdk.model.transaction;

import com.casper.sdk.model.common.Digest;
import com.casper.sdk.model.common.Ttl;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

/**
 * The header portion of a TransactionV1.
 *
 * @author ian@meywood.com
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TransactionV1Header {
    @JsonProperty("chain_name")
    private String chainName;
    @JsonProperty("timestamp")
    private Date timestamp;
    @JsonProperty("ttl")
    private Ttl ttl;
    @JsonProperty("body_hash")
    private Digest bodyHash;
    @JsonProperty("pricing_mode")
    private PricingMode pricingMode;
    @JsonProperty("initiator_addr")
    private InitiatorAddr initiatorAddr;
}

