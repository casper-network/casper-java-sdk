package com.casper.sdk.model.transaction.execution;

import com.casper.sdk.model.transaction.kind.Kind;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/**
 * Effects describe things that the creator of the effect intends to happen, producing a value upon completion.
 *
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
