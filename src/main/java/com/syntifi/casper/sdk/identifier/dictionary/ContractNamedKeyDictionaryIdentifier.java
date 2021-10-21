package com.syntifi.casper.sdk.identifier.dictionary;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.syntifi.casper.sdk.service.CasperService;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Identifier class passed to service
 * {@link CasperService#getDictionaryItem(String, ContractNamedKeyDictionaryIdentifier)}
 * to Lookup a dictionary item via a Contract named keys for dictionary item calls
 *
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
@Data
@AllArgsConstructor
public class ContractNamedKeyDictionaryIdentifier implements DictionaryIdentifier {
    @JsonProperty("ContractNamedKey")
    private ContractNamedKey contractNamedKey;
}
