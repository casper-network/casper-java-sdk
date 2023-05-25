package com.casper.sdk.model.status;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigInteger;

public class BlockSyncStatus {

    /**
     * Block Hash
     */
    @JsonProperty("block_hash")
    private String blockHash;

    /**
     * Block Height
     */
    @JsonProperty("block_height")
    private BigInteger blockHeight;

    /**
     * The state of acquisition of the data associated with the block
     */
    @JsonProperty("acquisition_state")
    private String acquisitionState;


}
