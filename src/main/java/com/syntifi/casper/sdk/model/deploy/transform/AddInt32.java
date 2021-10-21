package com.syntifi.casper.sdk.model.deploy.transform;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import lombok.Data;

/**
 * An implmentation of Transform that Adds the given `i32`
 * 
 * @see Transform
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
@Data
@JsonTypeName("AddInt32")
public class AddInt32 implements Transform {

    /**
     * i32
     */
    @JsonProperty("AddInt32")
    private long addInt32;
}
