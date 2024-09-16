package com.casper.sdk.model.entity.contract;

import com.casper.sdk.model.clvalue.AbstractCLValue;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/**
 * A key with a name
 *
 * @author carl@stormeye.co.uk
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NamedKey {

    @JsonProperty("named_key")
    private AbstractCLValue<?,?> namedKey;

    @JsonProperty("name")
    private AbstractCLValue<?,?> name;

}
