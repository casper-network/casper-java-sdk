package com.casper.sdk.model.entity.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

/**
 * @author carl@stormeye.co.uk
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Package {

    @JsonProperty("versions")
    private List<Versions> versions;

    @JsonProperty("disabled_versions")
    private List<Versions> disabledVersions;

    @JsonProperty("groups")
    private List<String> groups;

    @JsonProperty("lock_status")
    private PackageStatus lockStatus;

}
