package com.casper.sdk.service.result;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Account {

    private final String accountHash;
    private final List<NamedKey> namedKeys;
    private final String mainPurse;
    private final ActionThresholds actionThresholds;
    private final List<AssociatedKey> associatedKeys;

    @JsonCreator
    public Account(@JsonProperty("account_hash") final String accountHash,
                   @JsonProperty("named_keys") final List<NamedKey> namedKeys,
                   @JsonProperty("main_purse") final String mainPurse,
                   @JsonProperty("associated_keys") final List<AssociatedKey> associatedKeys,
                   @JsonProperty("action_thresholds") final ActionThresholds actionThresholds) {
        this.accountHash = accountHash;
        this.namedKeys = namedKeys;
        this.mainPurse = mainPurse;
        this.actionThresholds = actionThresholds;
        this.associatedKeys = associatedKeys;
    }

    public String getAccountHash() {
        return accountHash;
    }

    public List<NamedKey> getNamedKeys() {
        return namedKeys;
    }

    public List<AssociatedKey> getAssociatedKeys() {
        return associatedKeys;
    }

    public String getMainPurse() {
        return mainPurse;
    }

    public ActionThresholds getActionThresholds() {
        return actionThresholds;
    }
}
