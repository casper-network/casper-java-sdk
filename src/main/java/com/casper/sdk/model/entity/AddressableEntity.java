package com.casper.sdk.model.entity;

import com.casper.sdk.model.contract.EntryPoint;
import com.casper.sdk.model.contract.NamedKey;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressableEntity implements StateEntity {

    @JsonProperty("entity")
    private Entity entity;

    @JsonProperty("named_keys")
    private List<NamedKey> namedKeys;

    @JsonProperty("entry_points")
    private List<EntryPoint> entryPoints;

}
