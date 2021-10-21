package com.syntifi.casper.sdk.identifier.dictionary;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Contract named key for dictionary item calls
 *
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
@Data
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
