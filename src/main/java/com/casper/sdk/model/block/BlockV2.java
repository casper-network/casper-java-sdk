package com.casper.sdk.model.block;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/**
 * @author ian@meywood.com
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BlockV2 extends Block<BlockHeaderV2, BlockBodyV2> {

    @JsonProperty("header")
    private BlockHeaderV2 header;

    @JsonProperty("body")
    private BlockBodyV2 body;
}
