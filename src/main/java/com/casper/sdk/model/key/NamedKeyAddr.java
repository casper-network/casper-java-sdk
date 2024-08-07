package com.casper.sdk.model.key;

import com.casper.sdk.model.entity.EntityAddr;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * A NamedKey address.
 *
 * @author ian@meywood.com
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NamedKeyAddr {

    /** The address of the entity. */
    private EntityAddr entityAddr;
    /** The bytes of the name. */
    @JsonProperty("string_bytes")
    private String stringBytes;
}
