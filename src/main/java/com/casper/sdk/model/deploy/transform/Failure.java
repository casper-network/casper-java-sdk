package com.casper.sdk.model.deploy.transform;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * An implmentation of Transform that gives details about a failed
 * transformation,
 * containing an error message
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
@JsonTypeName("Failure")
public class Failure implements Transform {

    /**
     * error message
     */
    @JsonProperty("Failure")
    private String failure;
}
