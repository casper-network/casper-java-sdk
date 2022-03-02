package com.syntifi.casper.sdk.model.contract;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Contract version information
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
@Getter
@Setter
@Builder
public class ContractVersion {

    /**
     * contract_hash(string) The hash address of the contract
     */
    @JsonProperty("contract_hash")
    private String hash;

    /**
     * contract_version(integer)
     */
    @JsonProperty("contract_version")
    private int version;

    /**
     * protocol_version_major(integer)
     */
    @JsonProperty("protocol_version_major")
    private int protocolVersionMajor;
}
