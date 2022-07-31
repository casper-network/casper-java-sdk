package com.casper.sdk.model.block;

import com.casper.sdk.model.era.JsonEraEnd;
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
 * @see JsonBlock
 * @since 0.0.1
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JsonBlockHeader {

    /**
     * Block height
     */
    private long height;

    /**
     * Hex-encoded hash digest
     */
    @JsonProperty("state_root_hash")
    private String stateRootHash;

    /**
     * boolean
     */
    @JsonProperty("random_bit")
    private boolean randomBit;

    /**
     * @see JsonEraEnd
     */
    @JsonProperty("era_end")
    private JsonEraEnd eraEnd;

    /**
     * Hex-encoded hash digest
     */
    @JsonProperty("body_hash")
    private String bodyHash;

    /**
     * Hex-encoded hash digest.
     */
    @JsonProperty("parent_hash")
    private String parentHash;

    /**
     * Hex-encoded hash digest
     */
    @JsonProperty("accumulated_seed")
    private String accumulatedSeed;

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
