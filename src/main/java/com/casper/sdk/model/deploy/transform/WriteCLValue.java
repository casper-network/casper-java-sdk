package com.casper.sdk.model.deploy.transform;

import com.casper.sdk.model.clvalue.AbstractCLValue;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * An implementation of Transform that Writes the given CLValue to global state.
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
@JsonTypeName("WriteCLValue")
public class WriteCLValue implements Transform {

    /**
     * @see AbstractCLValue
     */
    @JsonProperty("WriteCLValue")
    private AbstractCLValue<?, ?> clvalue;
}
