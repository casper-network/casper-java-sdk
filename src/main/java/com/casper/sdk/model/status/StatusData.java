package com.casper.sdk.model.status;

import com.casper.sdk.model.key.PublicKey;
import com.casper.sdk.model.peer.PeerEntry;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

/**
 * Returns the current status of the node
 *
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StatusData {

    /**
     * The RPC API version
     */
    @JsonProperty("api_version")
    private String apiVersion;

    /**
     * The compiled node version
     */
    @JsonProperty("build_version")
    private String buildVersion;

    /**
     * The chainspec name
     */
    @JsonProperty("chainspec_name")
    private String chainSpecName;

    /**
     * @see MinimalBlockInfo
     */
    @JsonProperty("last_added_block_info")
    private MinimalBlockInfo lastAddedBlockInfo;

    /**
     * @see NextUpgrade
     */
    @JsonProperty("next_upgrade")
    private NextUpgrade nextUpgrade;

    /**
     * @see PublicKey
     */
    @JsonProperty("our_public_signing_key")
    private PublicKey publicKey;

    /**
     * List of
     *
     * @see PeerEntry
     */
    private List<PeerEntry> peers;

    /**
     * The next round length if this node is a validator
     */
    @JsonProperty("round_length")
    private String roundLength;

    /**
     * Time passed since the node has started
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String uptime;

    /**
     * The state root hash used at the start of the current session
     */
    @JsonProperty("starting_state_root_hash")
    private String startStateRootHash;

}
