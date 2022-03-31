package com.syntifi.casper.sdk.model.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Contract disabled version information
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
public class DisabledVersion {

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
