package com.casper.sdk.model.block;

import com.casper.sdk.model.common.Digest;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Date;

/**
 * @author ian@meywood.com
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BlockHeaderV2 extends BlockHeader {
    @JsonProperty("parent_hash")
    private Digest parentHash;

    @JsonProperty("state_root_hash")
    private Digest stateRootHash;

    @JsonProperty("body_hash")
    private Digest bodyHash;

    @JsonProperty("random_bit")
    private boolean randomBit;

    @JsonProperty("accumulated_seed")
    private String accumulatedSeed;

    @JsonProperty("era_end")
    private EraEndV2 eraEnd;

    @JsonProperty("timestamp")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private Date timeStamp;

    @JsonProperty("era_id")
    private long eraId;

    @JsonProperty("height")
    private long height;

    @JsonProperty("protocol_version")
    private String protocolVersion;

    @JsonProperty("proposer")
    private Digest proposer;

    @JsonProperty("current_gas_price")
    private long currentGasPrice;

    @JsonProperty("last_switch_block_hash")
    private Digest lastSwitchBlockHash;


}
