package com.casper.sdk.model.event.deployprocessed;

import com.casper.sdk.model.deploy.Operation;
import com.casper.sdk.model.deploy.transform.Transform;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExecutionEffect {

    @JsonProperty("operations")
    private List<Operation> operations;

    @JsonProperty("transforms")
    private List<Transform> transforms;
}
