package com.casper.sdk.model.event.transaction;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * The payload of the message emitted by an addressable entity during execution.
 *
 * @author ian@meywood.com
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
@JsonSubTypes({
        @JsonSubTypes.Type(value = StringMessagePayload.class, name = "String"),
        @JsonSubTypes.Type(value = BytesMessagePayload.class, name = "Bytes")})
public interface MessagePayload<T> {

    T getMessage();
}
