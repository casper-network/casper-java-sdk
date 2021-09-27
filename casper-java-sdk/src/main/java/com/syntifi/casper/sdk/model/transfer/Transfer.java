package com.syntifi.casper.sdk.model.transfer;
import java.math.BigInteger;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * Represents a transfer from one purse to another
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
@Data
public class Transfer {
    @JsonProperty("id") 
    private long id;

    /**
     * Hex-encoded account hash.
     */
    @JsonProperty("to")
    private String to;

    /**
     * Hex-encoded account hash.
     */
    @JsonProperty("from")
    private String from;

    /**
     * Amount transfered 
     */
    @JsonProperty("amount")
    private BigInteger amount;

    /**
     * Hex-encoded hash 
     */
    @JsonProperty("deploy_hash")
    private String deployHash;

    /**
     * Hex-encoded, formatted URef
     */
    @JsonProperty("source")
    private String source; 

    /**
     * Hex-encoded, formatted URef
     */
    @JsonProperty("target")
    private String target;

    /**
     * Decimal representation of a 512-bit integer.
     */
    @JsonProperty("gas")
    private BigInteger gas;
}

