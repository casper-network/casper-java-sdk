package com.casper.sdk.identifier.dictionary;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Contract named key for dictionary item calls
 *
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContractNamedKey {

    /**
     * The dictionary item key formatted as a string
     */
    @JsonProperty("dictionary_item_key")
    private String dictionaryItemKey;

    /**
     * The dictionary item key formatted as a string
     */
    @JsonProperty("dictionary_name")
    private String dictionaryName;

    /**
     * The contract key as a formatted string whose named keys contains
     * dictionary_name.
     */
    private String key;

}
