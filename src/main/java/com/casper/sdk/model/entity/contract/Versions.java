package com.casper.sdk.model.entity.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;


/**
 * Child of {@link Package}
 *
 * @author carl@stormeye.co.uk
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Versions {

    /** Major element of `ProtocolVersion` combined with `EntityVersion` */
    @JsonProperty("entity_version_key")
    private VersionKey entityVersionKey;

    /** Addressable Entity */
    @JsonProperty("addressable_entity_hash")
    private String addressableEntityHash;

}
