package com.casper.sdk.model.block;

import com.casper.sdk.model.common.Digest;
import com.casper.sdk.model.era.EraEndV2;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/**
 * V2 Block header.
 *
 * @author ian@meywood.com
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BlockHeaderV2 extends BlockHeader {

    @JsonProperty("era_end")
    private EraEndV2 eraEnd;

    @JsonProperty("proposer")
    private Digest proposer;

    @JsonProperty("current_gas_price")
    private long currentGasPrice;

    @JsonProperty("last_switch_block_hash")
    private Digest lastSwitchBlockHash;
}
