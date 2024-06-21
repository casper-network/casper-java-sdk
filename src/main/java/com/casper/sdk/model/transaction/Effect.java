package com.casper.sdk.model.transaction;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/**
 * @author ian@meywood.com
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Effect {

    @JsonProperty("key")
    private String key;

    @JsonProperty("kind")
    private Kind kind;

    public <T extends Kind> T getKind() {
        //noinspection unchecked
        return (T) kind;
    }
}
