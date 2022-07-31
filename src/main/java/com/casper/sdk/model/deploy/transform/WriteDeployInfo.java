package com.casper.sdk.model.deploy.transform;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.casper.sdk.model.deploy.DeployInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * An implementation of Transform that Writes the given DeployInfo to global
 * state.
 *
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @see Transform
 * @since 0.0.1
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonTypeName("WriteDeployInfo")
public class WriteDeployInfo implements Transform {

    /**
     * @see DeployInfo
     */
    @JsonProperty("WriteDeployInfo")
    private DeployInfo deployInfo;
}
