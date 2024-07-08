package com.casper.sdk.model.event.finalitysignature;

import com.casper.sdk.model.event.EventData;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Interface for V1 and V2 finality signatures.
 *
 * @author ian@meywood.com
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
@JsonSubTypes({
        @JsonSubTypes.Type(value = FinalitySignatureV1.class, name = "V1"),
        @JsonSubTypes.Type(value = FinalitySignatureV2.class, name = "V2")})
public interface FinalitySignature extends EventData {
}
