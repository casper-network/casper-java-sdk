package com.syntifi.casper.sdk.model.deploy.transform;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.syntifi.casper.sdk.model.contract.NamedKey;

import lombok.Data;

/**
 * An implmentation of Transform that Adds the given collection of named keys.
 * 
 * @see Transform
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
@Data
@JsonTypeName("AddKeys")
public class AddKeys implements Transform {

    /**
     * @see NamedKey
     */
    @JsonProperty("AddKeys")
    private List<NamedKey> addKeys;

}
