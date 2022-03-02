package com.syntifi.casper.sdk.model.storedvalue;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.syntifi.casper.sdk.model.account.Account;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Stored Value for {@link Account}
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @see StoredValue
 * @since 0.0.1
 */
@Getter
@Setter
@Builder
@JsonTypeName("Account")
public class StoredValueAccount implements StoredValue<Account> {
    @JsonProperty("Account")
    public Account value;
}
