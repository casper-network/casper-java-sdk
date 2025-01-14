package com.casper.sdk.model.deploy;

import com.casper.sdk.exception.CasperClientException;
import com.casper.sdk.exception.DynamicInstanceException;
import com.casper.sdk.model.uref.URef;
import com.fasterxml.jackson.annotation.*;
import lombok.*;

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
@JsonTypeName("Purse")
public class DelegatorKindPurse implements DelegatorKind {

    @JsonValue
    @Getter(AccessLevel.NONE)
    private String purseStr;
    @JsonIgnore
    private URef purse;

    @JsonCreator
    public DelegatorKindPurse(final String purse) {
        try {
            // Condor changes provides a UREF without prefix or access we therefore have to manually provide these
            this.purseStr = purse;
            this.purse = URef.fromString(purse.startsWith("uref-") ? purse : "uref-" + purse + "-000");
        } catch (IOException | DynamicInstanceException e) {
            throw new CasperClientException("Invalid purse bytes", e);
        }
    }
}
