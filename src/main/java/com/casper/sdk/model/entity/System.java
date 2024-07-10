package com.casper.sdk.model.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class System implements EntityAddressKind {

    @JsonProperty("System")
    private String system;

}
