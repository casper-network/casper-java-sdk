package com.casper.sdk.model.entity.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

/**
 * Package associated with a native contract implementation
 *
 * @author carl@stormeye.co.uk
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Package {

    /** All versions (enabled and disabled) */
    @JsonProperty("versions")
    private List<Versions> versions;

    /** Collection of disabled entity versions.
     * The runtime will not permit disabled entity versions to be executed */
    @JsonProperty("disabled_versions")
    private List<Versions> disabledVersions;

    /** Mapping maintaining the set of URefs associated with each "user group".
     *  This can be used to control access to methods in a particular version of the entity.
     *  A method is callable by any context which "knows" any of the URefs associated with the method's user group */
    @JsonProperty("groups")
    private List<String> groups;

    /** A flag that determines whether an entity is locked */
    @JsonProperty("lock_status")
    private PackageStatus lockStatus;

    /**
     * Determines the lock status of the package
     */
    public enum PackageStatus {
        // The package is locked and cannot be versioned
        Locked,
        // The package is unlocked and can be versioned
        Unlocked
    }
}
