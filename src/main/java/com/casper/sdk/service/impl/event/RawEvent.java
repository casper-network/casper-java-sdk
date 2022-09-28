package com.casper.sdk.service.impl.event;

import com.casper.sdk.model.event.DataType;
import com.casper.sdk.model.event.EventTarget;
import com.casper.sdk.model.event.EventType;
import lombok.ToString;

/**
 * An event that contains the event data as raw JSON without the 'data:' prefix is returned from the event service when
 * a {@link EventTarget#RAW} is requested.
 *
 * @author ian@meywood.com
 */
@ToString(doNotUseGetters = true, callSuper = true)
final class RawEvent extends AbstractEvent<String> {

    RawEvent(final EventType eventType, final String source, final Long id, final String data, final String version) {
        super(eventType, getDataType(data), source, id, data, version);
    }

    private static DataType getDataType(final String data) {
        final int start = data.indexOf(':');
        final int end = data.indexOf(':', start + 1);
        final String dataTypeName = data.substring(start + 3, end - 1);
        return DataType.of(dataTypeName.trim());
    }
}

