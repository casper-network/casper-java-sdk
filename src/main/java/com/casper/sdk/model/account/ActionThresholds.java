package com.casper.sdk.model.account;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Thresholds that have to be met when executing an action of a certain type.
 *
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 *
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ActionThresholds {

    /**
     *  Threshold for deploy execution.
     */
    @JsonProperty("deployment")
    private int deployment;

    /**
     * Threshold for managing action threshold.
     */
    @JsonProperty("key_management")
    private int keyManagement;

    /**
     * Threshold for upgrading contracts.
     */
    @JsonProperty("upgrade_management")
    private int upgradeManagement;

}
