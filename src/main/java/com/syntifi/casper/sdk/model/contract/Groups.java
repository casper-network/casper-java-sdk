package com.syntifi.casper.sdk.model.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Groups
 *
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Groups {

    /**
     * group(string)
     */
    @JsonProperty("group")
    private String name;

    /**
     * keys(Array/string) Hex-encoded, formatted URef.
     */
    @JsonProperty("keys")
    private List<String> keys;
}
