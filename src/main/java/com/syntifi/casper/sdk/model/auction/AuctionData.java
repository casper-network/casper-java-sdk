package com.syntifi.casper.sdk.model.auction;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * Root class for a Casper auction info request 
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
@Data
public class AuctionData {

    /**
     * The RPC API version 
     */
    @JsonProperty("api_version")
    private String apiVersion;

    /**
     * The auction state
     */
    @JsonProperty("auction_state")
    private AuctionState auctionState;
}
