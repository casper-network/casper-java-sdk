package com.casper.sdk.identifier.purse;

import com.casper.sdk.identifier.global.GlobalStateIdentifier;
import com.casper.sdk.model.uref.URef;
import com.casper.sdk.service.CasperService;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


/**
 * Identifier class passed to service
 * {@link CasperService#queryBalance(GlobalStateIdentifier, PurseIdentifier)} to identify the purse
 * and retrieve the balance given the purse URef
 *
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.2.0
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
public class PurseUref implements PurseIdentifier {

    @JsonUnwrapped
    @JsonProperty("purse_uref")
    private URef purseURef;
}
