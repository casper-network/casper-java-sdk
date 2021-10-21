package com.syntifi.casper.sdk.model.deploy;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.syntifi.casper.sdk.model.key.PublicKey;

import lombok.Data;

/**
 * The header portion of a [`Deploy`](struct.Deploy.html).
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
@Data
public class DeployHeader {

    /**
     * @see PublicKey
     */
    private PublicKey account; 

    /**
     * Body hash
     */
    @JsonProperty("body_hash")
    private String bodyHash;

    /**
     * Chain name
     */
    @JsonProperty("chain_name")
    private String chainName;

    /**
     * Dependencies
     */
    private List<String> dependencies;

    /**
     * Gas price 
     */
    @JsonProperty("gas_price")
    private BigInteger gasPrice; 

    /**
     * Timestamp formatted as per RFC 3339
     */
    @JsonProperty("timestamp")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private Date timeStamp;

    /**
     * Human-readable duration
     */
    private String ttl;
}
