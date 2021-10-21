package com.syntifi.casper.sdk.model.status;

import java.math.BigInteger;
import java.sql.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.syntifi.casper.sdk.model.key.PublicKey;
import com.syntifi.casper.sdk.model.peer.PeerData;

import lombok.Data;

/**
 * Minimal info of a `Block`
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @see PeerData
 * @since 0.0.1
 */
@Data
public class MinimalBlockInfo {

    /**
     * @see PublicKey
     */
    private PublicKey creator;

    /**
     * Era ID
     */
    @JsonProperty("era_id")
    private BigInteger eraId;

    /**
     * Block Hash
     */
    private String hash;

    /**
     * Block Height
     */
    private BigInteger height;

    /**
     * Hex-encoded hash digest
     */
    @JsonProperty("state_root_hash")
    private String stateRootHash;

    /**
     * Timestamp formatted as per RFC 3339
     */
    @JsonProperty("timestamp")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private Date timeStamp;
}