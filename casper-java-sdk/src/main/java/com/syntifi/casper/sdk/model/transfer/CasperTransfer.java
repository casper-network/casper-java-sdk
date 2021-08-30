package com.syntifi.casper.sdk.model.transfer;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * Casper transfer data
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
@Data
public class CasperTransfer {
    @JsonProperty("id") 
    private long id;

    @JsonProperty("to")
    private String to;

    @JsonProperty("from")
    private String from;

    @JsonProperty("amount")
    private Double amount;

    @JsonProperty("deploy_hash")
    private String deployHash;

    @JsonProperty("source")
    private String source; 

    @JsonProperty("target")
    private String target;

    @JsonProperty("gas")
    private int gas;
}

