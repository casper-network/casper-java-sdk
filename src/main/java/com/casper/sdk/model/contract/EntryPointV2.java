package com.casper.sdk.model.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EntryPointV2 implements EntryPoint{

    @JsonProperty("flags")
    private int flags;

    @JsonProperty("function_index")
    private int function_index;

}
