package com.syntifi.casper.sdk.identifier.block;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Identifier class passed to service
 * {@link com.syntifi.casper.sdk.service.CasperService#getBlock(BlockIdentifierByHash)}
 * to identify and retrieve the block given its hash.
 *
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
@Data
@AllArgsConstructor
public class BlockIdentifierByHash {

    /**
     * Block hash
     */
    @JsonProperty("Hash")
    private String hash;
}
