package com.casper.sdk.identifier.global;

import com.casper.sdk.service.CasperService;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


/**
 * Identifier class passed to service
 * {@link CasperService#queryGlobalState(GlobalStateIdentifier, String, String[])} to identify and
 * retrieve the global state given the block hash.
 *
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.2.0
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
public class BlockHashIdentifier implements GlobalStateIdentifier {

    /**
     * Block hash
     */
    @JsonProperty("BlockHash")
    private String hash;
}
