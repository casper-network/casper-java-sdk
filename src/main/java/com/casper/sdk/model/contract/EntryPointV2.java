package com.casper.sdk.model.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/**
 * The entry point for the V2 Casper VM.
 *
 * @author carl@stormeye.co.uk
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EntryPointV2 implements EntryPoint{

    /**
     * The flags
     */
    @JsonProperty("flags")
    private int flags;

    /**
     * The selector.
     */
    @JsonProperty("function_index")
    private int functionIndex;

}
