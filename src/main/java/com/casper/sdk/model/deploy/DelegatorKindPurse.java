package com.casper.sdk.model.deploy;

import com.casper.sdk.model.uref.URef;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/**
 * @author carl@stormeye.co.uk
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DelegatorKindPurse implements DelegatorKind {

    @JsonProperty("Purse")
    private URef purse;

}
