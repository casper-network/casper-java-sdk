package com.casper.sdk.model.entity.kind;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;


/**
 * The type of Package.
 *
 * @author carl@stormeye.co.uk
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
@JsonSubTypes({
        @JsonSubTypes.Type(value = EntityAccountKind.class, name = "Account"),
        @JsonSubTypes.Type(value = EntitySystemKind.class, name = "System"),
        @JsonSubTypes.Type(value = EntitySmartContractKind.class, name = "SmartContract")
})
public interface EntityAddressKind {
}
