package com.syntifi.casper.sdk.model.node;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.1.0
 */
@Data
public class CasperNodeData {
    @JsonProperty("api_version")
    private String apiVersion;
    
    private List<CasperNode> peers;
}
