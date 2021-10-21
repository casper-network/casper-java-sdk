package com.syntifi.casper.sdk.model.deploy;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The type of operation performed while executing a deploy.
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
public enum OpKind {
    @JsonProperty("Read")
    READ,
    @JsonProperty("Write")
    WRITE,
    @JsonProperty("Add")
    ADD, 
    @JsonProperty("NoOp")
    NOOP;
}
