package com.casper.sdk.model.transaction.kind;

import com.casper.sdk.model.entity.contract.Package;
import com.casper.sdk.model.storedvalue.StoredValue;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/**
 * @author carl@stormeye.co.uk
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PackageKind implements StoredValue<Package> {

    @JsonProperty("Package")
    private Package value;

}
