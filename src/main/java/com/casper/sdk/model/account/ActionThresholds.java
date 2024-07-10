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
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ActionThresholds {

    /**
     * deployment(Integer)
     */
    @JsonProperty("deployment")
    private int deployment;

    /**
     * key_management(Integer)
     */
    @JsonProperty("key_management")
    private int keyManagement;

    @JsonProperty("upgrade_management")
    private int upgradeManagement;

}
