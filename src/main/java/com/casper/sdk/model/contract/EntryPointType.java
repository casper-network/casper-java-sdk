package com.casper.sdk.model.contract;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Context of method execution
 *
 * @author ian@meywood.com
 */
public enum EntryPointType {

    @JsonProperty("Caller")
    CALLER,
    @JsonProperty("Called")
    CALLED,
    @JsonProperty("Factory")
    FACTORY
}
