package com.casper.sdk.model.transfer;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Root class for a Casper transfer request
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
public class TransferData {

    /**
     * The RPC API version
     */
    @JsonProperty("api_version")
    private String apiVersion;

    /**
     * Block hash
     */
    @JsonProperty("block_hash")
    private String blockHash;

    /**
     * List of @see Transfer
     */
    @JsonProperty("transfers")
    private List<Transfer> transfers;
}