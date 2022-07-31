package com.casper.sdk.model.status;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.casper.sdk.model.key.PublicKey;
import com.casper.sdk.model.peer.PeerData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;
import java.sql.Date;

/**
 * Minimal info of a `Block`
 *
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @see PeerData
 * @since 0.0.1
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
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