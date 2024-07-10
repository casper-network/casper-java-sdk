package com.casper.sdk.model.event.transaction;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Human-readable string message.
 *
 * @author ian@meywood.com
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonTypeName("String")
public class StringMessagePayload implements MessagePayload<String> {
    @JsonValue
    private String message;
}
