package com.casper.sdk.service.result;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class StoredValue {

    private final Account account;

    @JsonCreator
    public StoredValue(@JsonProperty("Account") Account account) {
        this.account = account;
    }

    public Account getAccount() {
        return account;
    }
}
