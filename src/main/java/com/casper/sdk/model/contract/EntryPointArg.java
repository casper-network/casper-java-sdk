package com.casper.sdk.model.contract;

import com.casper.sdk.model.clvalue.cltype.AbstractCLType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/**
 * A named argument for an entry point.
 *
 * @author ian@meywood.com
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class EntryPointArg {
    private String name;
    @JsonProperty("cl_type")
    private AbstractCLType clType;
}
