package com.casper.sdk.model.deploy;

import com.casper.sdk.exception.CasperClientException;
import com.casper.sdk.exception.DynamicInstanceException;
import com.casper.sdk.model.uref.URef;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;

/**
 * Delegation from purse
 *
 * @author carl@stormeye.co.uk
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
public class DelegatorKindPurse implements DelegatorKind {

    @JsonProperty("Purse")
    private URef purse;

    @JsonCreator
    public DelegatorKindPurse(final String purse) {
        try {
            this.purse = URef.fromString(purse);
        } catch (IOException | DynamicInstanceException e) {
            throw new CasperClientException("Invalid purse bytes", e);
        }
    }
}
