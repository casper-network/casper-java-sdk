package com.casper.sdk.model.block;

import com.casper.sdk.model.common.Digest;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author ian@meywood.com
 */
@Getter
@Setter
public class BlockHeader {

    @JsonProperty("parent_hash")
    private Digest parentHash;

    @JsonProperty("state_root_hash")
    private Digest stateRootHash;

    @JsonProperty("body_hash")
    private Digest bodyHash;

    @JsonProperty("random_bit")
    private boolean randomBit;

    @JsonProperty("height")
    private long height;

    @JsonProperty("accumulated_seed")
    private Digest accumulatedSeed;

    /**
     * Era ID newtype
     */
    @JsonProperty("era_id")
    private long eraId;

    /**
     * Timestamp formatted as per RFC 3339
     */
    @JsonProperty("timestamp")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private Date timeStamp;

    /**
     * Casper Platform protocol version
     */
    @JsonProperty("protocol_version")
    private String protocolVersion;
}

