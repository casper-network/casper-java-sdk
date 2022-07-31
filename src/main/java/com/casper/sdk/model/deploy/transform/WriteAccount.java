package com.casper.sdk.model.deploy.transform;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * An implementation of Transform that Writes the given Account to global state.
 *
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @see Transform
 * @since 0.0.1
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonTypeName("WriteAccount")
public class WriteAccount implements Transform {

    /**
     * Hex-encoded account hash
     */
    @JsonProperty("WriteAccount")
    private String account;
}
