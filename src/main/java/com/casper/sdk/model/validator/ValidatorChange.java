package com.casper.sdk.model.validator;


/**
 * A change to a validator's status between two eras
 *
 * @author alexandre carvalho
 * @author andre bertolace
 * @since 0.2.0
 */
public enum ValidatorChange {
    Added, Removed, Banned, CannotPropose, SeenAsFaulty
}
