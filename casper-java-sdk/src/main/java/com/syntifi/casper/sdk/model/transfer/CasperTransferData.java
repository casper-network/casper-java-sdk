package com.syntifi.casper.sdk.model.transfer;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

import lombok.Data;

/**
 * Root class for a Casper transfer request
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
@Data
public class CasperTransferData {
    
    @JsonProperty("api_version")
    private String apiVersion;
    
    @JsonProperty("block_hash")
    private String blockHash;
    
    @JsonProperty("transfers")
    private List<CasperTransfer> transfers;
}