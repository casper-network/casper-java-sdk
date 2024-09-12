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
public class VersionKey {

    @JsonProperty("protocol_version_major")
    private int protocolVersionMajor;

    @JsonProperty("entity_version")
    private int entityVersion;

}
