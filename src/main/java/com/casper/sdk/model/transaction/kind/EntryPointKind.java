package com.casper.sdk.model.transaction.kind;

import com.casper.sdk.model.contract.entrypoint.EntryPoint;
import com.casper.sdk.model.contract.entrypoint.EntryPointValue;
import com.casper.sdk.model.storedvalue.StoredValue;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/**
 * An EntryPointKind
 * See {@link EntryPoint}
 *
 * @author carl@stormeye.co.uk
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EntryPointKind implements StoredValue<EntryPointValue> {

    @JsonProperty("EntryPoint")
    private EntryPointValue value;

}
