package com.casper.sdk.service.result;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class PeerData extends AbstractResult {



    private final List<Peer> peers;

    @JsonCreator
    public PeerData(@JsonProperty("api_version") final String apiVersion, @JsonProperty("peers") final List<Peer> peers) {
        super(apiVersion);
        this.peers = peers;
    }

    public List<Peer> getPeers() {
        return peers;
    }
}
