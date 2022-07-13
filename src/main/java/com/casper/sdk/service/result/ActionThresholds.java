package com.casper.sdk.service.result;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ActionThresholds {

    private final int deployment;
    private final int keyManagement;

    @JsonCreator
    public ActionThresholds(@JsonProperty("deployment") int deployment,
                            @JsonProperty("key_management") int keyManagement) {
        this.deployment = deployment;
        this.keyManagement = keyManagement;
    }

    public int getDeployment() {
        return deployment;
    }

    public int getKeyManagement() {
        return keyManagement;
    }
}
