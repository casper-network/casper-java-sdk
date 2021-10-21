package com.syntifi.casper.sdk.identifier.dictionary;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.syntifi.casper.sdk.service.CasperService;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Identifier class passed to service
 * {@link CasperService#getDictionaryItem(String, AccountNamedKeyDictionaryIdentifier)}
 * to Lookup a dictionary item via an Account named key
 *
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
@Data
@AllArgsConstructor
public class AccountNamedKeyDictionaryIdentifier implements DictionaryIdentifier {
    @JsonProperty("AccountNamedKey")
    private AccountNamedKey accountNamedKey;
}
