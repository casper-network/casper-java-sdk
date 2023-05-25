package com.casper.sdk.model.status;


/**
 * The state of the reactor
 *
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 */
public enum ReactorState {
    Initialize,
    CatchUp,
    Upgrading,
    KeepUp,
    Validate,
    ShutdownForUpgrade;
}
