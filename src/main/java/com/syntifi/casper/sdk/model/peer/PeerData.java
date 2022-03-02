package com.syntifi.casper.sdk.model.peer;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Root class for a Casper peer info request
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @see PeerData
 * @since 0.0.1
 */
@Getter
@Setter
@Builder
public class PeerData {

    /**
     * The RPC API version
     */
    @JsonProperty("api_version")
    private String apiVersion;

    /**
     * List of @see PeerEntry
     */
    private List<PeerEntry> peers;
}
