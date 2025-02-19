package com.casper.sdk.model.entity;

import com.casper.sdk.model.contract.NamedKey;
import com.casper.sdk.model.contract.entrypoint.EntryPointValue;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.*;

import java.util.List;

/**
 * An addressable entity.
 * Methods and type signatures supported by a contract.
 *
 * @author carl@stormeye.co.uk
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressableEntity implements StateEntity {

    /** The addressable entity. */
    private Entity entity;

    /** The named keys of the addressable entity. */
    @JsonProperty("named_keys")
    private List<NamedKey> namedKeys;

    /** The entry points of the addressable entity. */
    @JsonProperty("entry_points")
    private List<EntryPointValue> entryPoints;
}
