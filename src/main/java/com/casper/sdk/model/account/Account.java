package com.casper.sdk.model.account;

import com.casper.sdk.model.contract.NamedKey;
import com.casper.sdk.model.entity.EntityAddressKind;
import com.casper.sdk.model.entity.StateEntity;
import com.casper.sdk.model.key.AccountHashKey;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

/**
 * Structure representing a user's account, stored in global state.
 *
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Account implements EntityAddressKind, StateEntity {

    /**
     * account_hash(String) Hex-encoded account hash.
     */
    @JsonProperty("account_hash")
    private AccountHashKey hash;

    /**
     * {@link ActionThresholds} that have to be met
     * when executing an action of a certain type.
     */
    @JsonProperty("action_thresholds")
    private ActionThresholds deployment;

    /**
     * a list of {@link AssociatedKey}
     */
    @JsonProperty("associated_keys")
    private List<AssociatedKey> associatedKeys;

    /**
     * main_purse(String) Hex-encoded, formatted URef.
     */
    @JsonProperty("main_purse")
    private String mainPurse;

    /**
     * named_keys (@link NamedKey)
     */
    @JsonProperty("named_keys")
    private List<NamedKey> namedKeys;
}
