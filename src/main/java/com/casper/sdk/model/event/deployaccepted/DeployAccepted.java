package com.casper.sdk.model.event.deployaccepted;

import com.casper.sdk.model.deploy.Deploy;
import com.casper.sdk.model.event.EventData;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The event stream server of the node emits a DeployAccepted event when a Deploy has been received by the node.
 *
 * @author ian@meywood.com
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonTypeName("DeployAccepted")
public class DeployAccepted implements EventData {

    /** The wrapped deploy */
    @JsonUnwrapped
    private Deploy deploy;
}
