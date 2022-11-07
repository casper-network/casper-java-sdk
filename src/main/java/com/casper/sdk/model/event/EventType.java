package com.casper.sdk.model.event;

/**
 * The type of event to obtain from a nodes event stream
 *
 * @author ian@meywood.com
 */
public enum EventType {

    /** Request deploy events from a stream */
    DEPLOYS,
    /** Request main events from a stream */
    MAIN,
    /** Request for signature events from a stream */
    SIGS
}
