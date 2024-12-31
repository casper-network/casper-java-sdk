package com.casper.sdk.model.deploy;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * @author carl@stormeye.co.uk
 */

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
@JsonSubTypes({
        @JsonSubTypes.Type(value = DelegatorKindPurse.class, name = "Purse"),
        @JsonSubTypes.Type(value = DelegatorKindPublicKey.class, name = "PublicKey")})
public interface DelegatorKind {}
