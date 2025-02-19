package com.casper.sdk.model.entity;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;


/**
 * The type of Package.
 *
 * @author carl@stormeye.co.uk
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
@JsonSubTypes({
        @JsonSubTypes.Type(value = AccountKind.class, name = "Account"),
        @JsonSubTypes.Type(value = SystemKind.class, name = "System"),
        @JsonSubTypes.Type(value = SmartContractKind.class, name = "SmartContract")
})
public interface EntityAddressKind {
}
