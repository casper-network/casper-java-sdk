package com.syntifi.casper.sdk.model.deploy.executabledeploy;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.syntifi.casper.sdk.model.deploy.NamedArg;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Abstract Executable Deploy Item containing the StoredVersionedContractByName.
 *
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 * @see ExecutableDeployItem
 */
@Getter
@Setter
@Builder
@JsonTypeName("StoredVersionedContractByName")
public class StoredVersionedContractByName implements ExecutableDeployItem {

    /**
     * List of @see NamedArg
     */
    private List<NamedArg<?, ?>> args;

    /**
     * Entry Point
     */
    @JsonProperty("entry_point")
    private String entryPoint;

    /**
     * Contract Name
     */
    private String name;

    /**
     * contract version
     */
    private long version;

}