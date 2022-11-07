package com.casper.sdk.model.event.shutdown;

import com.casper.sdk.model.event.EventData;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Event sent when a node shuts down
 *
 * @author ian@meywood.com
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@JsonTypeName("Shutdown")
public class Shutdown implements EventData {

}
