package com.casper.sdk.service.impl.event;

import com.casper.sdk.model.event.DataType;
import com.casper.sdk.model.event.EventTarget;
import lombok.ToString;

/**
 * An event that contains the event data as raw JSON without the 'data:' prefix is returned from the event service when
 * a {@link EventTarget#RAW} is requested.
 *
 * @author ian@meywood.com
 */
@ToString(doNotUseGetters = true, callSuper = true)
final class RawEvent extends AbstractEvent<String> {

    RawEvent(final String source, final Long id, final String data, final String version) {
        super(getDataType(data), source, id, data, version);
    }

    private static DataType getDataType(final String data) {

        final String replace = data.substring(2);
        final String[] split = replace.split(":");
        final String dataTypeName = split[0].substring(0, split[0].length() - 1);
        return DataType.of(dataTypeName.trim());
    }
}

