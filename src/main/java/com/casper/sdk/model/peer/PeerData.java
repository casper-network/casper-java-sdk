package com.casper.sdk.model.peer;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

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
@AllArgsConstructor
@NoArgsConstructor
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
