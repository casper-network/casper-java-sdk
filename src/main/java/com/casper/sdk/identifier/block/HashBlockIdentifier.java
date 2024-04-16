package com.casper.sdk.identifier.block;

import com.casper.sdk.service.CasperService;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Identifier class passed to service
 * {@link CasperService#getBlock(BlockIdentifier)} to identify and
 * retrieve the block given its hash.
 *
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@SuppressWarnings("JavadocDeclaration")
public class HashBlockIdentifier implements BlockIdentifier {

    /**
     * Block hash
     *
     * @param hash the block hash
     * @return the block hash
     */
    @JsonProperty("Hash")
    private String hash;
}
