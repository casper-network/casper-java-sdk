package com.casper.sdk.model.event.fault;

import com.casper.sdk.model.event.EventData;
import com.casper.sdk.model.key.PublicKey;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonTypeName("Fault")
public class Fault implements EventData {

    /** The period of time used to specify when specific events in a blockchain network occur. */
    @JsonProperty("era_id")
    private Long eraId;

    /** A unique personal address that is shared in the network. */
    @JsonProperty("public_key")
    private PublicKey publicKey;

    /** A timestamp type, representing a concrete moment in time of the fault. */
    @JsonProperty("timestamp")
    private String timestamp; // TODO convert to data
}
