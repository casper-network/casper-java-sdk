package com.casper.sdk.model.block;

import com.casper.sdk.model.key.PublicKey;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Abstract class for block body.
 *
 * @author ian@meywood.com
 */
@Getter
@Setter
public abstract class BlockBody {

    /** @see PublicKey */
    @JsonProperty("proposer")
    private PublicKey proposer;
}
