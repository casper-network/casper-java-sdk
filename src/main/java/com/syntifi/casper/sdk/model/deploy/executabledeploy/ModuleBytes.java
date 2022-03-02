package com.syntifi.casper.sdk.model.deploy.executabledeploy;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.syntifi.casper.sdk.model.deploy.NamedArg;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Abstract Executable Deploy Item containing the ModuleBytes of the contract.
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 * @see ExecutableDeployItem
 */
@Getter
@Setter
@Builder
@JsonTypeName("ModuleBytes")
public class ModuleBytes implements ExecutableDeployItem {

    /**
     * Module bytes
     */
    @JsonProperty("module_bytes")
    private String bytes;

    /**
     * @see NamedArg
     */
    private List<NamedArg<?, ?>> args;
}
