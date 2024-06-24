package com.casper.sdk.model.transaction;

import com.casper.sdk.model.storedvalue.StoredValue;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The Kind for a write
 *
 * @author ian@meywood.com
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class WriteKind<T> implements Kind {

    @JsonProperty("Write")
    private StoredValue<T> write;
}
