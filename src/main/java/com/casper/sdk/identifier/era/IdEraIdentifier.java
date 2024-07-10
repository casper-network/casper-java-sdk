package com.casper.sdk.identifier.era;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Identifier for an era using the era id.
 *
 * @author ian@meywood.com
 */
@AllArgsConstructor
@Getter
@Setter
@Builder
public class IdEraIdentifier implements EraIdentifier {
    @JsonProperty("Era")
    private long eraId;
}

