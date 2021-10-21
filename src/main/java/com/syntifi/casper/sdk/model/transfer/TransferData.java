package com.syntifi.casper.sdk.model.transfer;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * Root class for a Casper transfer request
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
@Data
public class TransferData {
    
    /**
     * The RPC API version
     */
    @JsonProperty("api_version")
    private String apiVersion;
   
    /**
     * Block hash
     */
    @JsonProperty("block_hash")
    private String blockHash;
   
    /**
     * List of @see Transfer
     */
    @JsonProperty("transfers")
    private List<Transfer> transfers;
}