package com.casper.sdk.model.bid;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.casper.sdk.model.key.PublicKey;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * An entry in a founding validator map representing a bid.
 *
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
