package com.syntifi.casper.sdk.identifier.block;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Identifier class passed to service
 * {@link com.syntifi.casper.sdk.service.CasperService#getBlock(BlockIdentifierByHeigh)}
 * to identify and retrieve the block given its height.
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
@Data
@AllArgsConstructor
public class BlockIdentifierByHeight {

    /**
     * Block height
     */
    @JsonProperty("Height")
    private long height;
}
