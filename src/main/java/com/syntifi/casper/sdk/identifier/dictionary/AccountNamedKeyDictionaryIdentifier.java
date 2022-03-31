package com.syntifi.casper.sdk.identifier.dictionary;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.syntifi.casper.sdk.service.CasperService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Identifier class passed to service
 * {@link CasperService#getStateDictionaryItem(String, DictionaryIdentifier)}
 * to look up a dictionary item via an Account named key
 *
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
public class AccountNamedKeyDictionaryIdentifier implements DictionaryIdentifier {
    @JsonProperty("AccountNamedKey")
    private AccountNamedKey accountNamedKey;
}
