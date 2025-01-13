package com.casper.sdk.model.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
/**
 * The entry point for the V2 Casper VM.
 *
 * TODO Delete this class there is no corresponding class in the Casper node
 *
 * @author carl@stormeye.co.uk
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EntryPointV2 {

    /** The flags */
    @JsonProperty("flags")
    private int flags;

    /** The selector. */
    @JsonProperty("function_index")
    private int functionIndex;
}
