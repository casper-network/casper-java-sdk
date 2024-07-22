package com.casper.sdk.model.contract;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/**
 * The encapsulated representation of entry points.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EntryPointValue {
    /** Entry points to be executed against the V1 Casper VM. */
    @JsonProperty("V1CasperVm")
    private EntryPoint v1;
    /** Entry points to be executed against the V2 Casper VM. */
    @JsonProperty("V2CasperVm")
    private EntryPointV2 v2;
}
