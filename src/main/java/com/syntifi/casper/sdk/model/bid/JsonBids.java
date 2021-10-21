package com.syntifi.casper.sdk.model.bid;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.syntifi.casper.sdk.model.key.PublicKey;

import lombok.Data;

/**
 * An entry in a founding validator map representing a bid.
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
@Data
public class JsonBids {

    /**
     * @see JsonBid
     */
    private JsonBid bid;

    /**
     * @see PublicKey
     */
    @JsonProperty("public_key")
    private PublicKey publicKey;
}
