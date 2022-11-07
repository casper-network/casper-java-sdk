package com.casper.sdk.model.event.version;


import com.casper.sdk.model.event.EventData;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.*;

/**
 * ApiVersion is always the first event emitted when a new client connects to the SSE server. It specifies the API
 * version of the server. The ApiVersion is the protocol version of a node on the Casper platform.
 *
 * @author ian@meywood.com
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonTypeName("ApiVersion")
public class ApiVersion implements EventData {

    @JsonValue
    private String apiVersion;
}
