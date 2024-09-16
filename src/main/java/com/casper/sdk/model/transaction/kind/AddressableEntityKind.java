package com.casper.sdk.model.transaction.kind;

import com.casper.sdk.model.entity.AddressableEntity;
import com.casper.sdk.model.entity.Entity;
import com.casper.sdk.model.storedvalue.StoredValue;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/**
 * An AddressableEntityKind
 * See {@link AddressableEntity}
 *
 * @author carl@stormeye.co.uk
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddressableEntityKind implements StoredValue<Entity> {

    @JsonProperty("AddressableEntity")
    private Entity value;

}
