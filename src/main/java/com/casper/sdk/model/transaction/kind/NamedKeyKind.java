package com.casper.sdk.model.transaction.kind;

import com.casper.sdk.model.entity.contract.NamedKey;
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
public class NamedKeyKind implements StoredValue<NamedKey> {

    @JsonProperty("NamedKey")
    private NamedKey value;

}
