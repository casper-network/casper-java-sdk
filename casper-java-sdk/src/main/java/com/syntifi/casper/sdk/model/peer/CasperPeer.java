package com.syntifi.casper.sdk.model.peer;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * Casper peer data
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @see CasperPeer
 * @since 0.0.1
 */
@Data
public class CasperPeer {

    private String address;
    
    @JsonProperty("node_id")
    private String nodeId;
}
