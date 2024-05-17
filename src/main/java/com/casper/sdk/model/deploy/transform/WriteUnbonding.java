package com.casper.sdk.model.deploy.transform;

import com.casper.sdk.model.deploy.UnbondingPurse;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.*;

import java.util.List;

/**
 * An implementation of Transform that Writes the given Unbonding to global state.
 *
 * @author Carl Norburn
 * @see Transform
 * @since 2.5.6
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonTypeName("WriteUnbonding")
public class WriteUnbonding implements Transform {

    @JsonProperty("WriteUnbonding")
    private List<UnbondingPurse> purses;

}
