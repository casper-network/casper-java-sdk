package com.casper.sdk.model.event.step;

import com.casper.sdk.model.deploy.ExecutionEffect;
import com.casper.sdk.model.event.EventData;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.*;

/**
 * The execution effects produced by a `StepRequest
 *
 * @author ian@meywood.com
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonTypeName("Step")
public class Step implements EventData {

    @JsonProperty("era_id")
    private long eraId;

    /** The journal of execution transforms from a single deploy. */
    @JsonProperty("execution_effect")
    private ExecutionEffect executionEffect;
}
