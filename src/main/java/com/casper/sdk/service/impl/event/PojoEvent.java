package com.casper.sdk.service.impl.event;

import com.casper.sdk.model.event.EventType;
import com.casper.sdk.model.event.EventData;
import com.casper.sdk.model.event.EventTarget;

/**
 * An event that contains the event data as Pojo model returned from the event service when
 * {@link EventTarget#POJO} is requested.
 *
 * @param <T> the types of the expected EventData
 * @author ian@meywood.com
 */

final class PojoEvent<T extends EventData> extends AbstractEvent<T> {

    PojoEvent(final EventType eventType, final String source, final Long id, T data) {
        super(eventType, source, id, data);
    }
}
