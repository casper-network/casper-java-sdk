package com.casper.sdk.model.entity.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/**
 * Child of {@link Versions}
 *
 * @author carl@stormeye.co.uk
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VersionKey {

    /** Major element of `ProtocolVersion` a `ContractVersion` is compatible with */
    @JsonProperty("protocol_version_major")
    private int protocolVersionMajor;

    /** Automatically incremented value for a contract version within a major `ProtocolVersion` */
    @JsonProperty("entity_version")
    private int entityVersion;

}
