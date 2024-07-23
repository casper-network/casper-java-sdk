package com.casper.sdk.model.event.blockadded;

import com.casper.sdk.model.block.Block;
import com.casper.sdk.model.common.Digest;
import com.casper.sdk.model.event.EventData;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.*;

/**
 * The BlockAdded event is emitted whenever a new block is added to the blockchain and stored locally in the node.
 *
 * @author ian@meywood.com
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonTypeName("BlockAdded")
public class BlockAdded implements EventData {

    /** A cryptographic hash identifying a `Block` */
    @JsonProperty("block_hash")
    private Digest blockHash;

    /** A JSON-friendly representation of `Block`. */
    @JsonProperty("block")
    private Block<?, ?> block;

    @JsonProperty("next_era_gas_price")
    private int nextEraGasPrice;

    public <T extends Block<?,?>> T getBlock() {
        //noinspection unchecked
        return (T) block;
    }
}
