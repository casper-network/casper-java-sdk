package com.casper.sdk.model.event.deployexpired;

import com.casper.sdk.model.common.Digest;
import com.casper.sdk.model.event.EventData;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The DeployExpired event is emitted when a Deploy becomes no longer valid to be executed or added to a block due to
 * their times to live (TTLs) expiring.
 *
 * @author ian@meywood.com
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonTypeName("DeployExpired")
public class DeployExpired implements EventData {

    /** The hash of the expired deploy */
    @JsonProperty("deploy_hash")
    private Digest deployHash;
}
