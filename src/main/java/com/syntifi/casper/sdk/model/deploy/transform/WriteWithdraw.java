package com.syntifi.casper.sdk.model.deploy.transform;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.syntifi.casper.sdk.model.deploy.UnbondingPurse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * An implementation of Transform that Writes the given Withdraw to global state.
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
@JsonTypeName("WriteWithdraw")
public class WriteWithdraw implements Transform {

    /**
     * Array of UnbondingPurse
     *
     * @see UnbondingPurse
     */
    @JsonProperty("WriteWithdraw")
    private List<UnbondingPurse> purses;
}
