package com.casper.sdk.model.entity;

import com.casper.sdk.exception.NoSuchKeyTagException;
import com.casper.sdk.model.key.AccountHashKey;
import com.casper.sdk.model.key.Key;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.*;

/**
 * Package associated with an Account hash.
 *
 * @author carl@stormeye.co.uk
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonTypeName("Account")
public class AccountKind implements EntityAddressKind {

    @JsonValue
    private AccountHashKey account;

    public AccountKind(final String accountSt) throws NoSuchKeyTagException {
        this.account = (AccountHashKey) Key.create(accountSt);
    }
}
