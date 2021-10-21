package com.syntifi.casper.sdk.model.deploy.transform;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.syntifi.casper.sdk.model.deploy.DeployInfo;

import lombok.Data;

/**
 * An implmentation of Transform that Writes the given DeployInfo to global state.
 * @see Transform
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
@Data
@JsonTypeName("WriteDeployInfo")
public class WriteDeployInfo implements Transform {
   
    /**
     * @see DeployInfo 
     */
    @JsonProperty("WriteDeployInfo")
    private DeployInfo deployInfo;
}



