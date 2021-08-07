package com.syntifi.casper.sdk.model.node;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.1.0
 */
@Data
public class CasperNode {
    private String address;
    
    @JsonProperty("node_id")
    private String nodeId;
}
