package com.syntifi.casper.sdk.model.deploy.transform;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.syntifi.casper.sdk.model.deploy.UnbondingPurse;

import lombok.Data;

/**
 * An implmentation of Transform that Writes the given Withdraw to global state.
 * 
 * @see Transform
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
@Data
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
