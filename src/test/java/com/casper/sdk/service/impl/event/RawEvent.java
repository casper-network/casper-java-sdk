package com.casper.sdk.service.impl.event;

import com.casper.sdk.model.event.EventType;
import com.casper.sdk.model.event.EventTarget;

/**
 * An event that contains the event data as raw JSON without the 'data:' prefix is returned from the event service when
 * a {@link EventTarget#RAW} is requested.
 *
 * @author ian@meywood.com
 */
final class RawEvent extends AbstractEvent<String> {

    RawEvent(final EventType eventType, final String data, final Long id) {
        super(eventType, data, id);
    }
}
