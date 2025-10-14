package com.casper.sdk.identifier.global;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class BlockHeightIdentifier implements GlobalStateIdentifier{

    @JsonProperty("BlockHeight")
    private long height;

}
