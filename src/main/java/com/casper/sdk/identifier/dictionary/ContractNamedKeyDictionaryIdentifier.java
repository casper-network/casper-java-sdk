package com.casper.sdk.identifier.dictionary;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.casper.sdk.service.CasperService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Identifier class passed to service
 * {@link CasperService#getStateDictionaryItem(String, DictionaryIdentifier)}
 * to Lookup a dictionary item via a Contract named keys for dictionary item
 * calls
 *
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
public class ContractNamedKeyDictionaryIdentifier implements DictionaryIdentifier {
    @JsonProperty("ContractNamedKey")
    private ContractNamedKey contractNamedKey;
}
