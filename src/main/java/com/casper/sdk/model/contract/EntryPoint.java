package com.casper.sdk.model.contract;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
@JsonSubTypes({
        @JsonSubTypes.Type(value = EntryPointV1.class, name = "V1CasperVm"),
        @JsonSubTypes.Type(value = EntryPointV2.class, name = "V2CasperVm")
})
public interface EntryPoint {}
