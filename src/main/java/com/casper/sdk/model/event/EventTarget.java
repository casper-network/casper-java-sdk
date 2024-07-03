package com.casper.sdk.model.event;

/**
 * The target type, that states how an event is to be
 *
 * @author ian@meywood.com
 */
public enum EventTarget {
    /** The event data is obtained as a raw JSON string */
    RAW,
    /** The event data in parsed from JSON to a Pojo */
    POJO
}
