package com.casper.sdk.model.transfer;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * A transfer of tokens from one account to another
 *
 * @author ian@meywood.com
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
@JsonSubTypes({
        @JsonSubTypes.Type(value = TransferV1.class, name = "Version1"),
        @JsonSubTypes.Type(value = TransferV2.class, name = "Version2")})
public interface Transfer {
}
