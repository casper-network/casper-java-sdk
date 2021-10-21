package com.syntifi.casper.sdk.identifier.block;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.syntifi.casper.sdk.service.CasperService;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Identifier class passed to service
 * {@link CasperService#getBlock(BlockIdentifierByHash)} to identify and
 * retrieve the block given its hash.
 *
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
@Data
@AllArgsConstructor
public class HashBlockIdentifier implements BlockIdentifier {

    /**
     * Block hash
     */
    @JsonProperty("Hash")
    private String hash;
}
