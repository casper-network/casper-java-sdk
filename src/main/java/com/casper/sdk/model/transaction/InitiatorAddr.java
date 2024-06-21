package com.casper.sdk.model.transaction;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The address of the transaction initiator.
 *
 * @author ian@meywood.com
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
@JsonSubTypes({
        @JsonSubTypes.Type(value = InitiatorPublicKey.class, name = "PublicKey"),
        @JsonSubTypes.Type(value = InitiatorAccountHash.class, name = "AccountHash")})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public abstract class InitiatorAddr<T> {
    @JsonValue
    private T address;
}
