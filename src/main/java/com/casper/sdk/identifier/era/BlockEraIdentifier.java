package com.casper.sdk.identifier.era;

import com.casper.sdk.identifier.block.BlockIdentifier;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Era identifier using an era's block identifier
 *
 * @author ian@meywood.com
 */
@AllArgsConstructor
@Getter
@Setter
@Builder
public class BlockEraIdentifier implements EraIdentifier {
    @JsonProperty("Block")
    private BlockIdentifier blockIdentifier;
}
