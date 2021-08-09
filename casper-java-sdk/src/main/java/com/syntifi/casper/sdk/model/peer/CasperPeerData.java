package com.syntifi.casper.sdk.model.peer;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * Root class for a Casper peer info request
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @see CasperPeerData
 * @since 0.0.1
 */
@Data
public class CasperPeerData {

    @JsonProperty("api_version")
    private String apiVersion;
    
    private List<CasperPeer> peers;
}
