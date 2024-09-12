package com.casper.sdk.model.entity.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/**
 * @author carl@stormeye.co.uk
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Versions {

    @JsonProperty("entity_version_key")
    private VersionKey entityVersionKey;

    @JsonProperty("addressable_entity_hash")
    private String addressableEntityHash;


}
