package com.casper.sdk.model.entity;

import com.casper.sdk.exception.NoSuchKeyTagException;
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
public class Account implements EntityAddressKind {

    @JsonValue
    private Key account;

    public Account(final String accountSt) throws NoSuchKeyTagException {
        this.account = Key.create(accountSt);
    }
}
