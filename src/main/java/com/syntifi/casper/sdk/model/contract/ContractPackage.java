package com.syntifi.casper.sdk.model.contract;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * Contract definition, metadata, and security container
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
@Data
public class ContractPackage {

    /**
     * access_key(string)
     */
    @JsonProperty("access_key")
    private String accessKey;

    /**
     * disabled_versions(Array/DisabledVersion)
     */
    @JsonProperty("disabled_versions")
    private List<DisabledVersion> disabledVersions;

    /**
     * groups(Array/Group)
     */
    @JsonProperty("groups")
    private List<Groups> groups;

    /**
     * versions(Array/ContractVersion)
     */
    @JsonProperty("versions")
    private List<ContractVersion> versions;
}
