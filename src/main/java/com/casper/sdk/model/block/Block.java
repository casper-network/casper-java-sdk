package com.casper.sdk.model.block;

import com.casper.sdk.model.common.Digest;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Abstract base class for all block versions.
 *
 * @author ian@meywood.com
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
@JsonSubTypes({
        @JsonSubTypes.Type(value = BlockV1.class, name = "Version1"),
        @JsonSubTypes.Type(value = BlockV2.class, name = "Version2")})
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public abstract class Block<HeaderT extends BlockHeader, BodyT extends BlockBody> {

    @JsonProperty("hash")
    private Digest hash;

    public abstract HeaderT getHeader();

    public abstract BodyT getBody();

}
