package com.syntifi.casper.sdk.model.auction;


import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.syntifi.casper.sdk.model.bid.JsonBids;
import com.syntifi.casper.sdk.model.block.JsonBlockData;
import com.syntifi.casper.sdk.model.era.JsonEraValidators;

import lombok.Data;

/**
 *  Data structure summarizing auction contract data
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @see JsonBlockData
 * @since 0.0.1
 */
@Data
public class AuctionState {

    /**
     * All bids contained within a vector
     * @see JsonBids
     */
    private List<JsonBids> bids;

    /**
     * Block height
     */
    @JsonProperty("block_height")
    private long height;

    /**
     * @see JsonEraValidators 
     */
    @JsonProperty("era_validators")
    private List<JsonEraValidators> eraValidators;

    /**
     * Global state hash 
     */
    @JsonProperty("state_root_hash")
    private String stateRootHash;

}