package com.syntifi.casper.sdk.model.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Contract definition, metadata, and security container
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
