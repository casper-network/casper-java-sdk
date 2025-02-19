package com.casper.sdk.model.entity;

import com.casper.sdk.model.account.Account;
import com.casper.sdk.model.account.ActionThresholds;
import com.casper.sdk.model.account.AssociatedKey;
import com.casper.sdk.model.contract.NamedKey;
import com.casper.sdk.model.key.AccountHashKey;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * An account entity
 *
 * @author carl@stormeye.co.uk
 */
@NoArgsConstructor
@JsonTypeName("Account")
public class AccountEntity extends Account implements StateEntity {

    public AccountEntity(final AccountHashKey hash,
                         final ActionThresholds deployment,
                         final List<AssociatedKey> associatedKeys,
                         final String mainPurse,
                         final List<NamedKey> namedKeys) {
        super(hash, deployment, associatedKeys, mainPurse, namedKeys);
    }
}
