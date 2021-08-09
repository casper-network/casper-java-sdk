package com.syntifi.casper.sdk.model.block;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.syntifi.casper.sdk.model.era.CasperEra;

import lombok.Data;

/**
 * Holds the header data of a Casper block
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @see CasperBlock
 * @since 0.0.1
 */
@Data
public class CasperBlockHeader {
    
    private long height;

    @JsonProperty("state_root_hash")
    private String stateRootHash;

    @JsonProperty("random_bit")
    private boolean randomBit;

    @JsonProperty("era_end")
    private CasperEra eraEnd;

    @JsonProperty("body_hash")
    private String bodyHash;

    @JsonProperty("parent_hash")
    private String parentHash;

    @JsonProperty("accumulated_seed")
    private String accumulatedSeed;

    private Date timestamp;

    @JsonProperty("era_id")
    private long eraId;

    @JsonProperty("protocol_version")
    private String protocolVersion;
}
