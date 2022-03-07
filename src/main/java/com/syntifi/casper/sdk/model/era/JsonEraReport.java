package com.syntifi.casper.sdk.model.era;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.syntifi.casper.sdk.model.key.PublicKey;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Equivocation and reward information to be included in the terminal block.
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @see JsonEraEnd
 * @since 0.0.1
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JsonEraReport {

    /**
     * List of @see PublicKey
     */
    @JsonProperty("inactive_validators")
    private List<PublicKey> inactiveValidators;

    /**
     * List of @see PublicKey
     */
    private List<PublicKey> equivocators;

    /**
     * List of @see Reward
     */
    private List<Reward> rewards;
}
