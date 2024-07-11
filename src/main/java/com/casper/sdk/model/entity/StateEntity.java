package com.casper.sdk.model.entity;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * An addressable entity or a legacy account.
 *
 * @author carl@stormeye.co.uk
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
@JsonSubTypes({
        @JsonSubTypes.Type(value = AddressableEntity.class, name = "AddressableEntity"),
        @JsonSubTypes.Type(value = LegacyAccount.class, name = "LegacyAccount")})
public interface StateEntity {}
