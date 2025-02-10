package com.casper.sdk.model.entity.kind;

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
public class EntityAccountKind implements EntityAddressKind {

    @JsonValue
    private AccountHashKey account;

    public EntityAccountKind(final String accountSt) throws NoSuchKeyTagException {
        this.account = (AccountHashKey) Key.create(accountSt);
    }
}
