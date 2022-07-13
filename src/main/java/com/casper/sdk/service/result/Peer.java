package com.casper.sdk.service.result;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Peer {

    private final String nodeId;

    public final String address;

    @JsonCreator
    public Peer(@JsonProperty("node_id") final String nodeId, @JsonProperty("address") final String address) {
        this.nodeId = nodeId;
        this.address = address;
    }

    public String getNodeId() {
        return nodeId;
    }

    public String getAddress() {
        return address;
    }
}
