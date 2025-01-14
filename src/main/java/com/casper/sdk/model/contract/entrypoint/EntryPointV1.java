package com.casper.sdk.model.contract.entrypoint;

import com.casper.sdk.jackson.deserializer.EntryPointV1Deserializer;
import com.casper.sdk.model.clvalue.cltype.AbstractCLType;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.*;

import java.util.List;

/**
 * Type signature of a method. Order of arguments matter since can be referenced by index as well as name.
 *
 * @author ian@meywood.com
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@JsonTypeName("entry_point")
@JsonDeserialize(using = EntryPointV1Deserializer.class)
public class EntryPointV1 {

    private String name;
    private EntryPointAccess access;
    private AbstractCLType ret;
    @JsonProperty("entry_point_type")
    private EntryPointType entryPointType;
    @JsonProperty("args")
    private List<EntryPointArg> args;
}
