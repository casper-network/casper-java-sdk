package com.casper.sdk.model.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

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

    /** access_key(string) */
    @JsonProperty("access_key")
    private String accessKey;

    /** disabled_versions(Array/DisabledVersion) */
    @JsonProperty("disabled_versions")
    private List<DisabledVersion> disabledVersions;

    /** The lock status of the contract package */
    @JsonProperty("lock_status")
    private String lockStatus;

    /** groups(Array/Group) */
    @JsonProperty("groups")
    private List<Groups> groups;

    /** versions(Array/ContractVersion) */
    @JsonProperty("versions")
    private List<ContractVersion> versions;
}
