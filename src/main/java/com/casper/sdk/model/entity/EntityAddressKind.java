package com.casper.sdk.model.entity;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Account.class, name = "Account"),
        @JsonSubTypes.Type(value = System.class, name = "System"),
        @JsonSubTypes.Type(value = SmartContract.class, name = "SmartContract")
})
public interface EntityAddressKind {}
