package com.casper.sdk.model.block;

import com.casper.sdk.model.common.Digest;
import com.casper.sdk.model.era.EraEndV1;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

/**
 * Holds the header data of a Casper block
 *
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @see BlockV1
 * @since 0.0.1
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JsonBlockHeader extends BlockHeader {

    /**
     * Block height
     */
    private long height;

    /**
     * Hex-encoded hash digest
     */
    @JsonProperty("state_root_hash")
    private Digest stateRootHash;

    /**
     * boolean
     */
    @JsonProperty("random_bit")
    private boolean randomBit;

    /**
     * @see EraEndV1
     */
    @JsonProperty("era_end")
    private EraEndV1 eraEnd;

    /**
     * Hex-encoded hash digest
     */
    @JsonProperty("body_hash")
    private Digest bodyHash;

    /**
     * Hex-encoded hash digest.
     */
    @JsonProperty("parent_hash")
    private Digest parentHash;

    /**
     * Hex-encoded hash digest
     */
    @JsonProperty("accumulated_seed")
    private Digest accumulatedSeed;

    /**
     * Timestamp formatted as per RFC 3339
     */
    @JsonProperty("timestamp")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private Date timeStamp;

    /**
     * Era ID newtype
     */
    @JsonProperty("era_id")
    private long eraId;

    /**
     * Casper Platform protocol version
     */
    @JsonProperty("protocol_version")
    private String protocolVersion;
}
